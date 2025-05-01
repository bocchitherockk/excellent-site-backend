package org.green_building.excellent_training.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.ParticipantRequestDto;
import org.green_building.excellent_training.dtos.ParticipantResponseDto;
import org.green_building.excellent_training.dtos.TrainingSessionResponseDto;
import org.green_building.excellent_training.entities.Participant;
import org.green_building.excellent_training.entities.Profile;
import org.green_building.excellent_training.entities.Structure;
import org.green_building.excellent_training.entities.TrainingSession;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.ParticipantsRepository;
import org.green_building.excellent_training.repositories.ProfilesRepository;
import org.green_building.excellent_training.repositories.StructuresRepository;
import org.green_building.excellent_training.repositories.TrainingSessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantsService {
    private final ParticipantsRepository participantsRepository;
    private final ProfilesRepository profilesRepository;
    private final StructuresRepository structuresRepository;
    private final TrainingSessionsRepository trainingSessionsRepository;

    @Autowired
    public ParticipantsService(ParticipantsRepository participantsRepository, ProfilesRepository profilesRepository, StructuresRepository structuresRepository, TrainingSessionsRepository trainingSessionsRepository) {
        this.participantsRepository = participantsRepository;
        this.profilesRepository = profilesRepository;
        this.structuresRepository = structuresRepository;
        this.trainingSessionsRepository = trainingSessionsRepository;
    }

    public List<ParticipantResponseDto> getAll() {
        List<Participant> participants = this.participantsRepository.findAll();
        return ParticipantResponseDto.from(participants);
    }

    public ParticipantResponseDto getById(Integer id) {
        Participant participant = this.participantsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("participant", "id", id));
        return ParticipantResponseDto.from(participant);
    }

    public List<TrainingSessionResponseDto> getParticipantTrainingSessionsById(Integer participantId) {
        Participant participant = this.participantsRepository.findById(participantId)
            .orElseThrow(() -> new ResourceNotFoundException("participant", "id", participantId));
        return TrainingSessionResponseDto.from(participant.getTrainingSessions());
    }

    public TrainingSessionResponseDto getParticipantTrainingSessionById(Integer participantId, Integer trainingSessionId) {
        Participant participant = this.participantsRepository.findById(participantId)
            .orElseThrow(() -> new ResourceNotFoundException("participant", "id", participantId));
        for (TrainingSession trainingSession : participant.getTrainingSessions()) {
            if (trainingSession.getId() == trainingSessionId)
                return TrainingSessionResponseDto.from(trainingSession);
        }
        throw new ResourceNotFoundException("training session", "id", trainingSessionId);
    }

    public ParticipantResponseDto create(ParticipantRequestDto request) {
        if (this.participantsRepository.existsByEmail(request.getEmail()))
            throw new NonUniqueValueException("participant", "email", request.getEmail());
        if (!this.profilesRepository.existsById(request.getProfileId()))
            // profile must exist in the db
            throw new ResourceNotFoundException("profile", "id", request.getProfileId());
        if (!this.structuresRepository.existsById(request.getStructureId()))
            // structure must exist in the db
            throw new ResourceNotFoundException("structure", "id", request.getStructureId());


        Participant participant = Participant.from(request);
        Participant createdParticipant = this.participantsRepository.save(participant);
        return ParticipantResponseDto.from(createdParticipant);
    }

    public List<TrainingSessionResponseDto> enrollParticipantInTrainingSessions(Integer participantId, List<Integer> trainingSessionsIds) {
        Participant participant = this.participantsRepository.findById(participantId)
        .orElseThrow(() -> new ResourceNotFoundException("participant", "id", participantId));

        // We need to work with TrainingSession entities since they own the relationship
        List<TrainingSession> trainingSessions = trainingSessionsRepository.findAllById(trainingSessionsIds);
        if (trainingSessions.size() != trainingSessionsIds.size()) {
            Set<Integer> existingIds = trainingSessions.stream().map(TrainingSession::getId).collect(Collectors.toSet());
            List<Integer> missingIds = trainingSessionsIds.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toList());
            throw new ResourceNotFoundException("training sessions", "ids", missingIds);
        }

        List<TrainingSession> enrolledSessions = new ArrayList<>();
        for (TrainingSession session : trainingSessions) {
            if (!session.getParticipants().contains(participant)) {
                session.getParticipants().add(participant);
                trainingSessionsRepository.save(session);
                enrolledSessions.add(session);
            } else {
                enrolledSessions.add(session); // Already enrolled
            }
        }

        return TrainingSessionResponseDto.from(enrolledSessions);
    }

    public TrainingSessionResponseDto enrollParticipantInTrainingSession(Integer participantId, Integer trainingSessionId) {
        Participant participant = this.participantsRepository.findById(participantId)
            .orElseThrow(() -> new ResourceNotFoundException("participant", "id", participantId));
        TrainingSession trainingSession = trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));

        if (!trainingSession.getParticipants().contains(participant)) {
            trainingSession.getParticipants().add(participant);
            trainingSessionsRepository.save(trainingSession);
        }

        return TrainingSessionResponseDto.from(trainingSession);
    }

    public ParticipantResponseDto updateById(Integer id, ParticipantRequestDto updates) {
        Participant participant = this.participantsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("participant", "id", id));
        if (updates.getEmail() != null && !updates.getEmail().equals(participant.getEmail())) {
            boolean emailExists = this.participantsRepository.existsByEmail(updates.getEmail());
            if (emailExists) {
                throw new NonUniqueValueException("participant", "email", updates.getEmail());
            }
            participant.setEmail(updates.getEmail());
        }
        if (updates.getFirstName()   != null) participant.setFirstName(updates.getFirstName());
        if (updates.getLastName()    != null) participant.setLastName(updates.getLastName());
        if (updates.getPhoneNumber() != null) participant.setPhoneNumber(updates.getPhoneNumber());
        if (updates.getProfileId()   != null) {
            Profile profile = this.profilesRepository.findById(updates.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("profile", "id", updates.getProfileId()));
            participant.setProfile(profile);
        }
        if (updates.getStructureId() != null) {
            Structure structure = this.structuresRepository.findById(updates.getStructureId())
                .orElseThrow(() -> new ResourceNotFoundException("structure", "id", updates.getStructureId()));
            participant.setStructure(structure);
        }
        Participant updatedParticipant = this.participantsRepository.save(participant);
        return ParticipantResponseDto.from(updatedParticipant);
    }

    public List<ParticipantResponseDto> deleteAll() {
        List<Participant> participants = this.participantsRepository.findAll();
        // Remove all participants from their associated training sessions
        participants.forEach(participant -> this.cancelParticipantFromTrainingSessions(participant.getId()));
        this.participantsRepository.deleteAll();
        return ParticipantResponseDto.from(participants);
    }

    public ParticipantResponseDto deleteById(Integer id) {
        Participant participant = this.participantsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("participant", "id", id));
        // Remove participant from all training sessions
        this.cancelParticipantFromTrainingSessions(id);
        this.participantsRepository.deleteById(id);
        return ParticipantResponseDto.from(participant);
    }

    public List<TrainingSessionResponseDto> cancelParticipantFromTrainingSessions(Integer participantId) {
        Participant participant = this.participantsRepository.findById(participantId)
            .orElseThrow(() -> new ResourceNotFoundException("participant", "id", participantId));

        List<TrainingSession> trainingSessions = participant.getTrainingSessions();
        List<TrainingSession> canceledSessions = new ArrayList<>(trainingSessions);

        for (TrainingSession session : trainingSessions) {
            session.getParticipants().remove(participant);
            trainingSessionsRepository.save(session);
        }

        return TrainingSessionResponseDto.from(canceledSessions);
    }

    public TrainingSessionResponseDto cancelParticipantFromTrainingSession(Integer participantId, Integer trainingSessionId) {
        Participant participant = this.participantsRepository.findById(participantId)
            .orElseThrow(() -> new ResourceNotFoundException("participant", "id", participantId));
        TrainingSession trainingSession = trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));

        if (trainingSession.getParticipants().remove(participant)) {
            trainingSessionsRepository.save(trainingSession);
            return TrainingSessionResponseDto.from(trainingSession);
        }

        throw new ResourceNotFoundException("training session", "id", trainingSessionId);
    }
}
