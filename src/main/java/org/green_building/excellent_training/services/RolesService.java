package org.green_building.excellent_training.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.green_building.excellent_training.entities.Role;
import org.green_building.excellent_training.repositories.RolesRepository;

@Service
public class RolesService {
    private final RolesRepository rolesRepository;

    @Autowired
    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<Role> getAll() {
        return this.rolesRepository.findAll();
    }

    public Role getById(Integer id) {
        // findById(id) returns Optional<Role>
        return this.rolesRepository.findById(id).orElse(null);
    }

    public Role create(Role role) {
        if (role.getName() == null) return null;
        return this.rolesRepository.save(role);
    }

    public Role updateById(Integer id, Role updates) {
        Role role = this.getById(id);
        if (role == null) return null;
        if (updates.getName() != null) role.setName(updates.getName());
        return this.rolesRepository.save(role);
    }

    public List<Role> deleteAll() {
        List<Role> roles = this.rolesRepository.findAll();
        this.rolesRepository.deleteAll();
        return roles;
    }

    public Role deleteById(Integer id) {
        Role role = this.getById(id); // if it doesn't exist, then this will be null
        this.rolesRepository.deleteById(id);
        return role;
    }
}
