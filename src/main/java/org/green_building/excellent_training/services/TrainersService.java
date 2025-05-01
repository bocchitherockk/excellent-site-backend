package org.green_building.excellent_training.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.TrainerRequestDto;
import org.green_building.excellent_training.dtos.TrainerResponseDto;
import org.green_building.excellent_training.dtos.TrainingSessionResponseDto;
import org.green_building.excellent_training.entities.Employer;
import org.green_building.excellent_training.entities.Trainer;
import org.green_building.excellent_training.entities.TrainingSession;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.EmployersRepository;
import org.green_building.excellent_training.repositories.TrainersRepository;
import org.green_building.excellent_training.repositories.TrainingSessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainersService {
    private final TrainersRepository trainersRepository;
    private final EmployersRepository employersRepository;
    private final TrainingSessionsRepository trainingSessionsRepository;

    @Autowired
    public TrainersService(TrainersRepository trainersRepository, EmployersRepository employersRepository , TrainingSessionsRepository trainingSessionsRepository) {
        this.trainersRepository = trainersRepository;
        this.employersRepository = employersRepository;
        this.trainingSessionsRepository = trainingSessionsRepository;
    }

    public List<TrainerResponseDto> getAll() {
        List<Trainer> trainers = this.trainersRepository.findAll();
        return TrainerResponseDto.from(trainers);
    }

    public TrainerResponseDto getById(Integer id) {
        Trainer trainer = this.trainersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("trainer", "id", id));
        return TrainerResponseDto.from(trainer);
    }

    public List<TrainingSessionResponseDto> getTrainerTrainingSessionsById(Integer trainerId) {
        Trainer trainer = this.trainersRepository.findById(trainerId)
            .orElseThrow(() -> new ResourceNotFoundException("trainer", "id", trainerId));
        return TrainingSessionResponseDto.from(trainer.getTrainingSessions());
    }

    public TrainingSessionResponseDto getTrainerTrainingSessionById(Integer trainerId, Integer trainingSessionId) {
        Trainer trainer = this.trainersRepository.findById(trainerId)
            .orElseThrow(() -> new ResourceNotFoundException("trainer", "id", trainerId));
        for (TrainingSession trainingSession : trainer.getTrainingSessions()) {
            if (trainingSession.getId() == trainingSessionId)
                return TrainingSessionResponseDto.from(trainingSession);
        }
        throw new ResourceNotFoundException("training session", "id", trainingSessionId);
    }

    public TrainerResponseDto create(TrainerRequestDto request) {
        if (this.trainersRepository.existsByEmail(request.getEmail()))
            throw new NonUniqueValueException("trainer", "email", request.getEmail());
        if (!this.employersRepository.existsById(request.getEmployerId()))
            // employer must exist in the db
            throw new ResourceNotFoundException("employer", "id", request.getEmployerId());

        Trainer trainer = Trainer.from(request);
        Trainer createdTrainer = this.trainersRepository.save(trainer);
        return TrainerResponseDto.from(createdTrainer);
    }

    public List<TrainingSessionResponseDto> assignTrainerToTrainingSessions(Integer trainerId, List<Integer> trainingSessionsIds) {
        Trainer trainer = this.trainersRepository.findById(trainerId)
        .orElseThrow(() -> new ResourceNotFoundException("trainer", "id", trainerId));

        // We need to work with TrainingSession entities since they own the relationship
        List<TrainingSession> trainingSessions = trainingSessionsRepository.findAllById(trainingSessionsIds);
        if (trainingSessions.size() != trainingSessionsIds.size()) {
            Set<Integer> existingIds = trainingSessions.stream().map(TrainingSession::getId).collect(Collectors.toSet());
            List<Integer> missingIds = trainingSessionsIds.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toList());
            throw new ResourceNotFoundException("training sessions", "ids", missingIds);
        }

        List<TrainingSession> assignedSessions = new ArrayList<>();
        for (TrainingSession session : trainingSessions) {
            if (!session.getTrainers().contains(trainer)) {
                session.getTrainers().add(trainer);
                trainingSessionsRepository.save(session);
                assignedSessions.add(session);
            } else {
                assignedSessions.add(session); // Already assigned
            }
        }

        return TrainingSessionResponseDto.from(assignedSessions);
    }

    public TrainingSessionResponseDto assignTrainerToTrainingSession(Integer trainerId, Integer trainingSessionId) {
        Trainer trainer = this.trainersRepository.findById(trainerId)
            .orElseThrow(() -> new ResourceNotFoundException("trainer", "id", trainerId));
        TrainingSession trainingSession = trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));

        if (!trainingSession.getTrainers().contains(trainer)) {
            trainingSession.getTrainers().add(trainer);
            trainingSessionsRepository.save(trainingSession);
        }

        return TrainingSessionResponseDto.from(trainingSession);
    }

    public TrainerResponseDto updateById(Integer id, TrainerRequestDto updates) {
        Trainer trainer = this.trainersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("trainer", "id", id));
        if (updates.getEmail() != null && !updates.getEmail().equals(trainer.getEmail())) {
            boolean emailExists = this.trainersRepository.existsByEmail(updates.getEmail());
            if (emailExists) {
                throw new NonUniqueValueException("trainer", "email", updates.getEmail());
            }
            trainer.setEmail(updates.getEmail());
        }
        if (updates.getFirstName()   != null) trainer.setFirstName(updates.getFirstName());
        if (updates.getLastName()    != null) trainer.setLastName(updates.getLastName());
        if (updates.getPhoneNumber() != null) trainer.setPhoneNumber(updates.getPhoneNumber());
        if (updates.getType()        != null) trainer.setType(updates.getType());
        if (updates.getEmployerId()  != null) {
            Employer employer = this.employersRepository.findById(updates.getEmployerId())
                .orElseThrow(() -> new ResourceNotFoundException("employer", "id", updates.getEmployerId()));
            trainer.setEmployer(employer);
        }
        Trainer updatedTrainer = this.trainersRepository.save(trainer);
        return TrainerResponseDto.from(updatedTrainer);
    }

    public List<TrainerResponseDto> deleteAll() {
        List<Trainer> trainers = this.trainersRepository.findAll();
        // Remove all trainers from their training sessions first
        trainers.forEach(trainer -> cancelTrainerFromTrainingSessions(trainer.getId()));
        this.trainersRepository.deleteAll();
        return TrainerResponseDto.from(trainers);
    }

    public TrainerResponseDto deleteById(Integer id) {
        Trainer trainer = this.trainersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("trainer", "id", id));
        // Remove the trainer from all training sessions (clears "trains" join table)
        this.cancelTrainerFromTrainingSessions(id);
        this.trainersRepository.deleteById(id);
        return TrainerResponseDto.from(trainer);
    }

    public List<TrainingSessionResponseDto> cancelTrainerFromTrainingSessions(Integer trainerId) {
        Trainer trainer = this.trainersRepository.findById(trainerId)
            .orElseThrow(() -> new ResourceNotFoundException("trainer", "id", trainerId));

        List<TrainingSession> trainingSessions = trainer.getTrainingSessions();
        List<TrainingSession> canceledSessions = new ArrayList<>(trainingSessions);

        for (TrainingSession session : trainingSessions) {
            session.getTrainers().remove(trainer);
            trainingSessionsRepository.save(session);
        }

        return TrainingSessionResponseDto.from(canceledSessions);
    }

    public TrainingSessionResponseDto cancelTrainerFromTrainingSession(Integer trainerId, Integer trainingSessionId) {
        Trainer trainer = this.trainersRepository.findById(trainerId)
            .orElseThrow(() -> new ResourceNotFoundException("trainer", "id", trainerId));
        TrainingSession trainingSession = trainingSessionsRepository.findById(trainingSessionId)
            .orElseThrow(() -> new ResourceNotFoundException("training session", "id", trainingSessionId));

        if (trainingSession.getTrainers().remove(trainer)) {
            trainingSessionsRepository.save(trainingSession);
            return TrainingSessionResponseDto.from(trainingSession);
        }

        throw new ResourceNotFoundException("training session", "id", trainingSessionId);
    }
}
