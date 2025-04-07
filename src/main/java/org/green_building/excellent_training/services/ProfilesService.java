package org.green_building.excellent_training.services;

import java.util.List;

import org.green_building.excellent_training.dtos.ProfileRequestDto;
import org.green_building.excellent_training.dtos.ProfileResponseDto;
import org.green_building.excellent_training.entities.Profile;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.ProfilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfilesService {
    private final ProfilesRepository profilesRepository;

    @Autowired
    public ProfilesService(ProfilesRepository profilesRepository) {
        this.profilesRepository = profilesRepository;
    }

    public List<ProfileResponseDto> getAll() {
        List<Profile> profiles = this.profilesRepository.findAll();
        return ProfileResponseDto.from(profiles);
    }

    public ProfileResponseDto getById(Integer id) {
        Profile profile = this.profilesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));
        return ProfileResponseDto.from(profile);
    }

    public ProfileResponseDto create(ProfileRequestDto request) {
        if (this.profilesRepository.existsByName(request.getName())) {
            // request.getName() is not null because it is checked by request validation mechanism
            throw new NonUniqueValueException("profile", "name", request.getName());
        }
        Profile profile = Profile.from(request);
        Profile createdProfile = this.profilesRepository.save(profile);
        return ProfileResponseDto.from(createdProfile);
    }

    public ProfileResponseDto updateById(Integer id, ProfileRequestDto updates) {
        Profile profile = this.profilesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));
        if (updates.getName() != null && !updates.getName().equals(profile.getName())) {
            if (this.profilesRepository.existsByName(updates.getName()))
                throw new NonUniqueValueException("profile", "name", updates.getName());
            profile.setName(updates.getName());
        }
        Profile updatedProfile = this.profilesRepository.save(profile);
        return ProfileResponseDto.from(updatedProfile);
    }

    public List<ProfileResponseDto> deleteAll() {
        List<Profile> profiles = this.profilesRepository.findAll();
        this.profilesRepository.deleteAll();
        return ProfileResponseDto.from(profiles);
    }

    public ProfileResponseDto deleteById(Integer id) {
        Profile profile = this.profilesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));
        this.profilesRepository.deleteById(id);
        return ProfileResponseDto.from(profile);
    }
}
