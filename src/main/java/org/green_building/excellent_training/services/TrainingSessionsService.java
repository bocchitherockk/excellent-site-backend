package org.green_building.excellent_training.services;

import java.util.List;

import org.green_building.excellent_training.dtos.TrainingSessionRequestDto;
import org.green_building.excellent_training.dtos.TrainingSessionResponseDto;
import org.green_building.excellent_training.entities.Domain;
import org.green_building.excellent_training.entities.TrainingSession;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.DomainsRepository;
import org.green_building.excellent_training.repositories.TrainingSessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingSessionsService {
    private final TrainingSessionsRepository trainingSessionsRepository;
    private final DomainsRepository domainsRepository;

    @Autowired
    public TrainingSessionsService(TrainingSessionsRepository trainingSessionsRepository, DomainsRepository domainsRepository) {
        this.trainingSessionsRepository = trainingSessionsRepository;
        this.domainsRepository = domainsRepository;
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
}
