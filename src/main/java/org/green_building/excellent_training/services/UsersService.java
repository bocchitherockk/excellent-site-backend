package org.green_building.excellent_training.services;

import java.util.List;

import org.green_building.excellent_training.dtos.UserRequestDto;
import org.green_building.excellent_training.dtos.UserResponseDto;
import org.green_building.excellent_training.entities.Role;
import org.green_building.excellent_training.entities.User;
import org.green_building.excellent_training.exceptions.NonUniqueValueException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.RolesRepository;
import org.green_building.excellent_training.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository, RolesRepository rolesRepository) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
    }

    public List<UserResponseDto> getAll() {
        List<User> users = this.usersRepository.findAll();
        return UserResponseDto.from(users);
    }

    public UserResponseDto getById(Integer id) {
        User user = this.usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return UserResponseDto.from(user);
    }

    public UserResponseDto create(UserRequestDto request) {
        if (this.usersRepository.existsByUsername(request.getUsername()))
            throw new NonUniqueValueException("user", "username", request.getUsername());
        if (!this.rolesRepository.existsById(request.getRoleId()))
            // role must exist in the db
            throw new ResourceNotFoundException("role", "id", request.getRoleId());
        User user = User.from(request);
        User createdUser = this.usersRepository.save(user);
        return UserResponseDto.from(createdUser);
    }

    public UserResponseDto updateById(Integer id, UserRequestDto updates) {
        User user = this.usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        if (updates.getUsername() != null && !updates.getUsername().equals(user.getUsername())) {
            boolean usernameExists = this.usersRepository.existsByUsername(updates.getUsername());
            if (usernameExists) {
                throw new NonUniqueValueException("user", "username", updates.getUsername());
            }
            user.setUsername(updates.getUsername());
        }
        if (updates.getPassword() != null) user.setPassword(updates.getPassword());
        if (updates.getRoleId() != null) {
            Role role = this.rolesRepository.findById(updates.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("role", "id", updates.getRoleId()));
            user.setRole(role);
        }
        User updatedUser = this.usersRepository.save(user);
        return UserResponseDto.from(updatedUser);
    }

    public List<UserResponseDto> deleteAll() {
        List<User> users = this.usersRepository.findAll();
        this.usersRepository.deleteAll();
        return UserResponseDto.from(users);
    }

    public UserResponseDto deleteById(Integer id) {
        User user = this.usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        this.usersRepository.deleteById(id);
        return UserResponseDto.from(user);
    }
}
