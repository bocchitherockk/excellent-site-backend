package org.green_building.excellent_training.services;

import java.util.List;

import org.green_building.excellent_training.dtos.DomainRequestDto;
import org.green_building.excellent_training.dtos.DomainResponseDto;
import org.green_building.excellent_training.entities.Domain;
import org.green_building.excellent_training.entities.TrainingSession;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.DomainsRepository;
import org.green_building.excellent_training.repositories.TrainingSessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainsService {
    private final DomainsRepository domainsRepository;
    private final TrainingSessionsRepository trainingSessionsRepository;

    @Autowired
    public DomainsService(DomainsRepository domainsRepository, 
                          TrainingSessionsRepository trainingSessionsRepository) {
        this.domainsRepository = domainsRepository;
        this.trainingSessionsRepository = trainingSessionsRepository;
    }

    public List<DomainResponseDto> getAll() {
        List<Domain> domains = this.domainsRepository.findAll();
        return DomainResponseDto.from(domains);
    }

    public DomainResponseDto getById(Integer id) {
        Domain domain = this.domainsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("domain", "id", id));
        return DomainResponseDto.from(domain);
    }

    public DomainResponseDto create(DomainRequestDto request) {
        if (this.domainsRepository.existsByName(request.getName())) {
            // request.getName() is not null because it is checked by request validation mechanism
            throw new NonUniqueValueException("domain", "name", request.getName());
        }
        Domain domain = Domain.from(request);
        Domain createdDomain = this.domainsRepository.save(domain);
        return DomainResponseDto.from(createdDomain);
    }

    public DomainResponseDto updateById(Integer id, DomainRequestDto updates) {
        Domain domain = this.domainsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("domain", "id", id));
        if (updates.getName() != null && !updates.getName().equals(domain.getName())) {
            if (this.domainsRepository.existsByName(updates.getName()))
                throw new NonUniqueValueException("domain", "name", updates.getName());
            domain.setName(updates.getName());
        }
        Domain updatedDomain = this.domainsRepository.save(domain);
        return DomainResponseDto.from(updatedDomain);
    }

    public List<DomainResponseDto> deleteAll() {
        List<Domain> domains = this.domainsRepository.findAll();

        // Delete all training sessions for all domains first
        domains.forEach(domain -> {
            List<TrainingSession> sessions = domain.getTrainingSessions();
            trainingSessionsRepository.deleteAll(sessions);
        });

        this.domainsRepository.deleteAll();
        return DomainResponseDto.from(domains);
    }

    public DomainResponseDto deleteById(Integer id) {
        Domain domain = this.domainsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("domain", "id", id));

         // Delete all training sessions associated with the domain
        List<TrainingSession> sessions = domain.getTrainingSessions();
        trainingSessionsRepository.deleteAll(sessions); // Safely delete sessions

        this.domainsRepository.deleteById(id);
        return DomainResponseDto.from(domain);
    }
}
