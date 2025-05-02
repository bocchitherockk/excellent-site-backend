package org.green_building.excellent_training.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.EmployerRequestDto;
import org.green_building.excellent_training.dtos.EmployerResponseDto;
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
public class EmployersService {
    private final EmployersRepository employersRepository;
    private final TrainersRepository trainersRepository;
    private final TrainingSessionsRepository trainingSessionsRepository;

    @Autowired
    public EmployersService(EmployersRepository employersRepository, TrainersRepository trainersRepository, 
                            TrainingSessionsRepository trainingSessionsRepository) {
        this.employersRepository = employersRepository;
        this.trainersRepository = trainersRepository;
        this.trainingSessionsRepository = trainingSessionsRepository;
    }

    public List<EmployerResponseDto> getAll() {
        List<Employer> employers = this.employersRepository.findAll();
        return EmployerResponseDto.from(employers);
    }

    public EmployerResponseDto getById(Integer id) {
        Employer employer = this.employersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("employer", "id", id));
        return EmployerResponseDto.from(employer);
    }

    public EmployerResponseDto create(EmployerRequestDto request) {
        if (this.employersRepository.existsByName(request.getName())) {
            // request.getName() is not null because it is checked by request validation mechanism
            throw new NonUniqueValueException("employer", "name", request.getName());
        }
        Employer employer = Employer.from(request);
        Employer createdEmployer = this.employersRepository.save(employer);
        return EmployerResponseDto.from(createdEmployer);
    }

    public EmployerResponseDto updateById(Integer id, EmployerRequestDto updates) {
        Employer employer = this.employersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("employer", "id", id));
        if (updates.getName() != null && !updates.getName().equals(employer.getName())) {
            if (this.employersRepository.existsByName(updates.getName()))
                throw new NonUniqueValueException("employer", "name", updates.getName());
            employer.setName(updates.getName());
        }
        Employer updatedEmployer = this.employersRepository.save(employer);
        return EmployerResponseDto.from(updatedEmployer);
    }

    public List<EmployerResponseDto> deleteAll() {
        List<Employer> employers = this.employersRepository.findAll();
         employers.forEach(employer -> {

             // Remove trainers from sessions and delete them
             List<Trainer> trainers = employer.getTrainers();
            trainers.forEach(trainer -> {
                List<TrainingSession> sessions = trainer.getTrainingSessions();
                sessions.forEach(session -> session.getTrainers().remove(trainer));
                trainingSessionsRepository.saveAll(sessions);
            });
            trainersRepository.deleteAll(trainers);
        });
this.employersRepository.deleteAll();
        return EmployerResponseDto.from(employers);
    }

    public EmployerResponseDto deleteById(Integer id) {
        Employer employer = this.employersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("employer", "id", id));

         // Remove all trainers from their training sessions first
        List<Trainer> trainers = employer.getTrainers();
        trainers.forEach(trainer -> {
            // Remove trainer from all training sessions (clears "trains" join table)
            List<TrainingSession> sessions = trainer.getTrainingSessions();
            sessions.forEach(session -> session.getTrainers().remove(trainer));
            trainingSessionsRepository.saveAll(sessions); // Flush changes
        });

        // Delete all trainers associated with the employer
        trainersRepository.deleteAll(trainers);

        this.employersRepository.deleteById(id);
        return EmployerResponseDto.from(employer);
    }

    public Map<String, List<?>> getEmployersWithTrainersCount() {
        Map<String, List<?>> result = new HashMap<>();
        List<String> employerNames = new ArrayList<>();
        List<Integer> trainerCounts = new ArrayList<>();
        List<Employer> employers = employersRepository.findAll();
        for (Employer employer : employers) {
            employerNames.add(employer.getName());
            Integer trainerCount = trainersRepository.countByEmployerId(employer.getId());
            trainerCounts.add(trainerCount);
        }
        result.put("names", employerNames);
        result.put("trainers_count", trainerCounts);
        return result;
    }
}
