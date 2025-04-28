package org.green_building.excellent_training.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.ParticipantResponseDto;
import org.green_building.excellent_training.dtos.TrainerResponseDto;
import org.green_building.excellent_training.dtos.TrainingSessionRequestDto;
import org.green_building.excellent_training.dtos.TrainingSessionResponseDto;
import org.green_building.excellent_training.entities.Domain;
import org.green_building.excellent_training.entities.Participant;
import org.green_building.excellent_training.entities.Trainer;
import org.green_building.excellent_training.entities.TrainingSession;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.DomainsRepository;
import org.green_building.excellent_training.repositories.ParticipantsRepository;
import org.green_building.excellent_training.repositories.TrainersRepository;
import org.green_building.excellent_training.repositories.TrainingSessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingSessionsService {
    private final TrainingSessionsRepository trainingSessionsRepository;
    private final DomainsRepository domainsRepository;
    private final ParticipantsRepository participantsRepository;
    private final TrainersRepository trainersRepository;

    @Autowired
    public TrainingSessionsService(TrainingSessionsRepository trainingSessionsRepository, DomainsRepository domainsRepository, ParticipantsRepository participantsRepository, TrainersRepository trainersRepository) {
        this.trainingSessionsRepository = trainingSessionsRepository;
        this.domainsRepository = domainsRepository;
        this.participantsRepository = participantsRepository;
        this.trainersRepository = trainersRepository;
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

    public List<TrainerResponseDto> getTrainingSessionTrainersById(Integer trainingSessionId) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        return TrainerResponseDto.from(trainingSession.getTrainers());
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

    public TrainerResponseDto getTrainingSessionTrainerById(Integer trainingSessionId, Integer trainerId) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        for (Trainer trainer : trainingSession.getTrainers()) {
            if (trainer.getId().equals(trainerId)) {
                return TrainerResponseDto.from(trainer);
            }
        }
        throw new ResourceNotFoundException("trainer", "id", trainerId);
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

    public List<TrainerResponseDto> assignTrainersToTrainingSession(Integer trainingSessionId, List<Integer> trainersIds) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        List<Trainer> trainers = this.trainersRepository.findAllById(trainersIds);
        if (trainers.size() != trainersIds.size()) {
            Set<Integer> existingIds = trainers.stream().map(Trainer::getId).collect(Collectors.toSet());
            List<Integer> missingIds = trainersIds.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toList());
            throw new ResourceNotFoundException("trainers", "ids", missingIds);
        }

        Set<Trainer> currentTrainers = new HashSet<>(trainingSession.getTrainers());

        List<Trainer> addedTrainers = trainers.stream()
            .filter(currentTrainers::add)
            .peek(trainingSession.getTrainers()::add)
            .collect(Collectors.toList());

        this.trainingSessionsRepository.save(trainingSession);
        return TrainerResponseDto.from(addedTrainers);
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

    public TrainerResponseDto assignTrainerToTrainingSession(Integer trainingSessionId, Integer trainerId) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        Trainer trainer = this.trainersRepository.findById(trainerId)
            .orElseThrow(() -> new ResourceNotFoundException("trainer", "id", trainerId));

        if (trainingSession.getTrainers().contains(trainer)) {
            return TrainerResponseDto.from(trainer);
        }

        trainingSession.getTrainers().add(trainer);
        this.trainingSessionsRepository.save(trainingSession);
        return TrainerResponseDto.from(trainer);
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

    public List<TrainerResponseDto> cancelTrainersFromTrainingSession(Integer trainingSessionId) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        List<Trainer> trainers = new ArrayList<>(trainingSession.getTrainers());
        trainingSession.getTrainers().clear();
        this.trainingSessionsRepository.save(trainingSession);
        return TrainerResponseDto.from(trainers);
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

    public TrainerResponseDto cancelTrainerFromTrainingSession(Integer trainingSessionId, Integer trainerId) {
        TrainingSession trainingSession = this.trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));
        Trainer trainer = this.trainersRepository.findById(trainerId)
            .orElseThrow(() -> new ResourceNotFoundException("trainer", "id", trainerId));

        if (trainingSession.getTrainers().remove(trainer)) {
            this.trainingSessionsRepository.save(trainingSession);
            return TrainerResponseDto.from(trainer);
        }

        throw new ResourceNotFoundException("trainer", "id", trainerId);
    }

    /**
     * Gets the sum of budgets for training sessions grouped by domain for the past 5 years
     * @return Map with domain names as keys and lists of yearly budget sums as values
     */
    public Map<String, List<Double>> getSessionsBudgetSumByDomainForPastFiveYears() {
        Map<String, List<Double>> result = new HashMap<>();

        Integer currentYear = LocalDate.now().getYear();
        List<Domain> domains = domainsRepository.findAll();
        for (Domain domain : domains) {
            String domainName = domain.getName();
            List<Double> yearlyBudgets = new ArrayList<>();
            for (int i = 4; i >= 0; i--) {
                Integer year = currentYear - i;
                Double budgetSum = trainingSessionsRepository.sumBudgetByDomainAndYear(domain.getId(), year);
                yearlyBudgets.add(budgetSum);
            }
            result.put(domainName, yearlyBudgets);
        }
        return result;
    }
}
