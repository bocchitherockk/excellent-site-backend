package org.green_building.excellent_training.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.ParticipantResponseDto;
import org.green_building.excellent_training.dtos.TrainingSessionRequestDto;
import org.green_building.excellent_training.dtos.TrainingSessionResponseDto;
import org.green_building.excellent_training.entities.Domain;
import org.green_building.excellent_training.entities.Participant;
import org.green_building.excellent_training.entities.TrainingSession;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.DomainsRepository;
import org.green_building.excellent_training.repositories.ParticipantsRepository;
import org.green_building.excellent_training.repositories.TrainingSessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingSessionsService {
    private final TrainingSessionsRepository trainingSessionsRepository;
    private final DomainsRepository domainsRepository;
    private final ParticipantsRepository participantsRepository;

    @Autowired
    public TrainingSessionsService(TrainingSessionsRepository trainingSessionsRepository, DomainsRepository domainsRepository, ParticipantsRepository participantsRepository) {
        this.trainingSessionsRepository = trainingSessionsRepository;
        this.domainsRepository = domainsRepository;
        this.participantsRepository = participantsRepository;
    }

    public List<TrainingSessionResponseDto> getAll() {
        List<TrainingSession> trainingSessions = this.trainingSessionsRepository.findAll();
        return TrainingSessionResponseDto.from(trainingSessions);
    }

    public TrainingSessionResponseDto getById(Integer id) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", id));
        return TrainingSessionResponseDto.from(trainingSession);
    }

    public List<ParticipantResponseDto> getTrainingSessionParticipantsById(Integer trainingSessionId) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        return ParticipantResponseDto.from(trainingSession.getParticipants());
    }

    public ParticipantResponseDto getTrainingSessionParticipantById(Integer trainingSessionId, Integer participantId) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        for (Participant participant : trainingSession.getParticipants()) {
            if (participant.getId().equals(participantId)) {
                return ParticipantResponseDto.from(participant);
            }
        }
        throw new ResourceNotFoundException("participant", "id", participantId);
    }

    public TrainingSessionResponseDto create(TrainingSessionRequestDto request) {
        /* note: this is in case you want to make the title unique
        if (this.trainingSessionsRepository.existsByTitle(request.getTitle()))
            throw new NonUniqueValueException("training session", "title", request.getEmail());
        */
        if (!this.domainsRepository.existsById(request.getDomainId()))
            // domain must exist in the db
            throw new ResourceNotFoundException("domain", "id", request.getDomainId());

        TrainingSession trainingSession = TrainingSession.from(request);
        TrainingSession createdTrainingSession = this.trainingSessionsRepository.save(trainingSession);
        return TrainingSessionResponseDto.from(createdTrainingSession);
    }

    public List<ParticipantResponseDto> enrollParticipantsInTrainingSession(Integer trainingSessionId, List<Integer> participantsIds) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        List<Participant> participants = this.participantsRepository.findAllById(participantsIds);
        if (participants.size() != participantsIds.size()) {
            Set<Integer> existingIds = participants.stream().map(Participant::getId).collect(Collectors.toSet());
            List<Integer> missingIds = participantsIds.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toList());
            throw new ResourceNotFoundException("participants", "ids", missingIds);
        }

        Set<Participant> currentParticipants = new HashSet<>(trainingSession.getParticipants());

        List<Participant> addedParticipants = participants.stream()
            .filter(currentParticipants::add)
            .peek(trainingSession.getParticipants()::add)
            .collect(Collectors.toList());

        this.trainingSessionsRepository.save(trainingSession);
        return ParticipantResponseDto.from(addedParticipants);
    }

    public ParticipantResponseDto enrollParticipantInTrainingSession(Integer trainingSessionId, Integer participantId) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        Participant participant = this.participantsRepository.findById(participantId)
            .orElseThrow(() -> new ResourceNotFoundException("participant", "id", participantId));

        if (trainingSession.getParticipants().contains(participant)) {
            return ParticipantResponseDto.from(participant);
        }

        trainingSession.getParticipants().add(participant);
        this.trainingSessionsRepository.save(trainingSession);
        return ParticipantResponseDto.from(participant);
    }

    public TrainingSessionResponseDto updateById(Integer id, TrainingSessionRequestDto updates) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", id));
        /* note: this is in case you want to make the title unique
        if (updates.getTitle() != null && !updates.getTitle().equals(trainingSession.getTitle())) {
            boolean titleExists = this.trainingSessionsRepository.existsByTitle(updates.getTitle());
            if (titleExists) {
                throw new NonUniqueValueException("training session", "title", updates.getTitle());
            }
            trainingSession.setTitle(updates.getTitle());
        }
        */
        if (updates.getTitle() != null) trainingSession.setTitle(updates.getTitle());
        if (updates.getYear() != null) trainingSession.setYear(updates.getYear());
        if (updates.getDurationDays() != null) trainingSession.setDurationDays(updates.getDurationDays());
        if (updates.getBudget() != null) trainingSession.setBudget(updates.getBudget());
        if (updates.getDomainId() != null) {
            Domain domain = this.domainsRepository.findById(updates.getDomainId())
                .orElseThrow(() -> new ResourceNotFoundException("domain", "id", updates.getDomainId()));
            trainingSession.setDomain(domain);
        }

        TrainingSession updatedTrainingSession = this.trainingSessionsRepository.save(trainingSession);
        return TrainingSessionResponseDto.from(updatedTrainingSession);
    }

    public List<TrainingSessionResponseDto> deleteAll() {
        List<TrainingSession> trainingSessions = this.trainingSessionsRepository.findAll();
        this.trainingSessionsRepository.deleteAll();
        return TrainingSessionResponseDto.from(trainingSessions);
    }

    public TrainingSessionResponseDto deleteById(Integer id) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", id));
        this.trainingSessionsRepository.deleteById(id);
        return TrainingSessionResponseDto.from(trainingSession);
    }

    public List<ParticipantResponseDto> cancelParticipantsFromTrainingSession(Integer trainingSessionId) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        List<Participant> participants = new ArrayList<>(trainingSession.getParticipants());
        trainingSession.getParticipants().clear();
        this.trainingSessionsRepository.save(trainingSession);
        return ParticipantResponseDto.from(participants);
    }

    public ParticipantResponseDto cancelParticipantFromTrainingSession(Integer trainingSessionId, Integer participantId) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        Participant participant = this.participantsRepository.findById(participantId)
            .orElseThrow(() -> new ResourceNotFoundException("participant", "id", participantId));

        if (trainingSession.getParticipants().remove(participant)) {
            this.trainingSessionsRepository.save(trainingSession);
            return ParticipantResponseDto.from(participant);
        }

        throw new ResourceNotFoundException("participant", "id", participantId);
    }
}
