package org.green_building.excellent_training.services;

import java.util.List;

import org.green_building.excellent_training.dtos.RoleRequestDto;
import org.green_building.excellent_training.dtos.RoleResponseDto;
import org.green_building.excellent_training.entities.Role;
import org.green_building.excellent_training.entities.User;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.RolesRepository;
import org.green_building.excellent_training.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesService {
    private final RolesRepository rolesRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public RolesService(RolesRepository rolesRepository, 
                        UsersRepository usersRepository) {
        this.rolesRepository = rolesRepository;
        this.usersRepository = usersRepository;
    }

    public List<RoleResponseDto> getAll() {
        List<Role> roles = this.rolesRepository.findAll();
        return RoleResponseDto.from(roles);
    }

    public RoleResponseDto getById(Integer id) {
        Role role = this.rolesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("role", "id", id));
        return RoleResponseDto.from(role);
    }

    public RoleResponseDto create(RoleRequestDto request) {
        if (this.rolesRepository.existsByName(request.getName())) {
            // request.getName() is not null because it is checked by request validation mechanism
            throw new NonUniqueValueException("role", "name", request.getName());
        }
        Role role = Role.from(request);
        Role createdRole = this.rolesRepository.save(role);
        return RoleResponseDto.from(createdRole);
    }

    public RoleResponseDto updateById(Integer id, RoleRequestDto updates) {
        Role role = this.rolesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("role", "id", id));
        if (updates.getName() != null && !updates.getName().equals(role.getName())) {
            if (this.rolesRepository.existsByName(updates.getName()))
                throw new NonUniqueValueException("role", "name", updates.getName());
            role.setName(updates.getName());
        }
        Role updatedRole = this.rolesRepository.save(role);
        return RoleResponseDto.from(updatedRole);
    }

    public List<RoleResponseDto> deleteAll() {
        List<Role> roles = this.rolesRepository.findAll();

        // Delete all users linked to all roles
        roles.forEach(role -> {
            List<User> users = role.getUsers();
            usersRepository.deleteAll(users);
        });

        this.rolesRepository.deleteAll();
        return RoleResponseDto.from(roles);
    }

    public RoleResponseDto deleteById(Integer id) {
        Role role = this.rolesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("role", "id", id));

        // Delete all users linked to this role
        List<User> users = role.getUsers(); // Requires @OneToMany in Role entity
        usersRepository.deleteAll(users);

        this.rolesRepository.deleteById(id);
        return RoleResponseDto.from(role);
    }
}
