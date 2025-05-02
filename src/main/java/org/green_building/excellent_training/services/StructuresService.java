package org.green_building.excellent_training.services;

import java.util.List;

import org.green_building.excellent_training.dtos.StructureRequestDto;
import org.green_building.excellent_training.dtos.StructureResponseDto;
import org.green_building.excellent_training.entities.Participant;
import org.green_building.excellent_training.entities.Structure;
import org.green_building.excellent_training.entities.TrainingSession;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.ParticipantsRepository;
import org.green_building.excellent_training.repositories.StructuresRepository;
import org.green_building.excellent_training.repositories.TrainingSessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StructuresService {
    private final StructuresRepository structuresRepository;
    private final ParticipantsRepository participantsRepository;
    private final TrainingSessionsRepository trainingSessionsRepository;

    @Autowired
    public StructuresService(StructuresRepository structuresRepository, 
                             ParticipantsRepository participantsRepository,
                             TrainingSessionsRepository trainingSessionsRepository) {
        this.structuresRepository = structuresRepository;
        this.participantsRepository = participantsRepository;
        this.trainingSessionsRepository = trainingSessionsRepository;
    }

    public List<StructureResponseDto> getAll() {
        List<Structure> structures = this.structuresRepository.findAll();
        return StructureResponseDto.from(structures);
    }

    public StructureResponseDto getById(Integer id) {
        Structure structure = this.structuresRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("structure", "id", id));
        return StructureResponseDto.from(structure);
    }

    public StructureResponseDto create(StructureRequestDto request) {
        if (this.structuresRepository.existsByName(request.getName())) {
            // request.getName() is not null because it is checked by request validation mechanism
            throw new NonUniqueValueException("structure", "name", request.getName());
        }
        Structure structure = Structure.from(request);
        Structure createdStructure = this.structuresRepository.save(structure);
        return StructureResponseDto.from(createdStructure);
    }

    public StructureResponseDto updateById(Integer id, StructureRequestDto updates) {
        Structure structure = this.structuresRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("structure", "id", id));
        if (updates.getName() != null && !updates.getName().equals(structure.getName())) {
            if (this.structuresRepository.existsByName(updates.getName()))
                throw new NonUniqueValueException("structure", "name", updates.getName());
            structure.setName(updates.getName());
        }
        Structure updatedStructure = this.structuresRepository.save(structure);
        return StructureResponseDto.from(updatedStructure);
    }

    public List<StructureResponseDto> deleteAll() {
        List<Structure> structures = this.structuresRepository.findAll();

        structures.forEach(structure -> {
            List<Participant> participants = structure.getParticipants();
            participants.forEach(participant -> {
                List<TrainingSession> sessions = participant.getTrainingSessions();
                sessions.forEach(session -> session.getParticipants().remove(participant));
                trainingSessionsRepository.saveAll(sessions);
            });
            participantsRepository.deleteAll(participants);
        });

        this.structuresRepository.deleteAll();
        return StructureResponseDto.from(structures);
    }

    public StructureResponseDto deleteById(Integer id) {
        Structure structure = this.structuresRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("structure", "id", id));

         // Delete participants and their associations
        List<Participant> participants = structure.getParticipants();
        participants.forEach(participant -> {
            List<TrainingSession> sessions = participant.getTrainingSessions();
            sessions.forEach(session -> session.getParticipants().remove(participant));
            trainingSessionsRepository.saveAll(sessions);
        });
        participantsRepository.deleteAll(participants);

        this.structuresRepository.deleteById(id);
        return StructureResponseDto.from(structure);
    }
}
