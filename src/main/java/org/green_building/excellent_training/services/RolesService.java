package org.green_building.excellent_training.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.green_building.excellent_training.entities.Role;
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
        // findById(id) returns Optional<Role>
        Role role = this.rolesRepository.findById(id).orElse(null);
        return RoleDto.from(role);
    }

    public RoleDto create(RoleDto roleDto) {
        if (roleDto.getName() == null/* || this.rolesRepository.findByName(roleDto.getName()).isPresent()*/) return null; // TODO: check this commented line, in case in the future you want to check for unique values(if you will then do that across all the projects) (if ou will do so then make sure to give back useful response errors to the frontend in case of a fail)
        Role role = Role.from(roleDto);
        Role createdRole = this.rolesRepository.save(role);
        return RoleDto.from(createdRole);
    }

    public RoleDto updateById(Integer id, RoleDto updatesDto) {
        Role updates = Role.from(updatesDto);
        Role role = this.rolesRepository.findById(id).orElse(null);
        if (role == null) return null;
        if (updates.getName() != null) role.setName(updates.getName());
        Role updatedRole = this.rolesRepository.save(role);
        return RoleDto.from(updatedRole);
    }

    public List<RoleDto> deleteAll() {
        List<Role> roles = this.rolesRepository.findAll();
        this.rolesRepository.deleteAll();
        return RoleDto.from(roles);
    }

    public RoleDto deleteById(Integer id) {
        RoleDto roleDto = this.getById(id); // if it doesn't exist, then this will be null
        if (roleDto == null) return null;
        this.rolesRepository.deleteById(id);
        return roleDto;
    }
}
