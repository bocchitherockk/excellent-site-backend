package org.green_building.excellent_training.services;

import java.util.List;

import org.green_building.excellent_training.dtos.ParticipantRequestDto;
import org.green_building.excellent_training.dtos.ParticipantResponseDto;
import org.green_building.excellent_training.entities.Participant;
import org.green_building.excellent_training.entities.Profile;
import org.green_building.excellent_training.entities.Structure;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.ParticipantsRepository;
import org.green_building.excellent_training.repositories.ProfilesRepository;
import org.green_building.excellent_training.repositories.StructuresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantsService {
    private final ParticipantsRepository participantsRepository;
    private final ProfilesRepository profilesRepository;
    private final StructuresRepository structuresRepository;

    @Autowired
    public ParticipantsService(ParticipantsRepository participantsRepository, ProfilesRepository profilesRepository, StructuresRepository structuresRepository) {
        this.participantsRepository = participantsRepository;
        this.profilesRepository = profilesRepository;
        this.structuresRepository = structuresRepository;
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
        if (updates.getFirstName() != null) participant.setFirstName(updates.getFirstName());
        if (updates.getLastName() != null) participant.setLastName(updates.getLastName());
        if (updates.getPhoneNumber() != null) participant.setPhoneNumber(updates.getPhoneNumber());
        if (updates.getProfileId() != null) {
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
        this.participantsRepository.deleteAll();
        return ParticipantResponseDto.from(participants);
    }

    public ParticipantResponseDto deleteById(Integer id) {
        Participant participant = this.participantsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("participant", "id", id));
        this.participantsRepository.deleteById(id);
        return ParticipantResponseDto.from(participant);
    }
}
