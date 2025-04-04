package org.green_building.excellent_training.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.green_building.excellent_training.entities.Role;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.dtos.RoleDto;
import org.green_building.excellent_training.repositories.RolesRepository;

@Service
public class RolesService {
    private final RolesRepository rolesRepository;

    @Autowired
    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<RoleDto> getAll() {
        List<Role> roles = this.rolesRepository.findAll();
        return RoleDto.from(roles);
    }

    public RoleDto getById(Integer id) {
        Role role = this.rolesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("role", "id", id));
        return RoleDto.from(role);
    }

    public RoleDto create(RoleDto roleDto) {
        if (this.rolesRepository.existsByName(roleDto.getName())) {
            // roleDto.getName() is not null because it is checked by request validation mechanism
            throw new NonUniqueValueException("role", "name", roleDto.getName());
        }
        Role role = Role.from(roleDto);
        Role createdRole = this.rolesRepository.save(role);
        return RoleDto.from(createdRole);
    }

    public RoleDto updateById(Integer id, RoleDto updatesDto) {
        Role role = this.rolesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("role", "id", id));
        if (updatesDto.getName() != null && !updatesDto.getName().equals(role.getName())) {
            boolean nameExists = this.rolesRepository.existsByName(updatesDto.getName());
            if (nameExists) {
                throw new NonUniqueValueException("role", "name", updatesDto.getName());
            }
            role.setName(updatesDto.getName());
        }
        Role updatedRole = this.rolesRepository.save(role);
        return RoleDto.from(updatedRole);
    }

    public List<RoleDto> deleteAll() {
        List<Role> roles = this.rolesRepository.findAll();
        this.rolesRepository.deleteAll();
        return RoleDto.from(roles);
    }

    public RoleDto deleteById(Integer id) {
        Role role = this.rolesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("role", "id", id));
        this.rolesRepository.deleteById(id);
        return RoleDto.from(role);
    }
}
