package org.green_building.excellent_training.services;

import java.util.List;

import org.green_building.excellent_training.dtos.UserDto;
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

    public List<UserDto> getAll() {
        List<User> users = this.usersRepository.findAll();
        return UserDto.from(users);
    }

    public UserDto getById(Integer id) {
        User user = this.usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return UserDto.from(user);
    }

    public UserDto create(UserDto userDto) {
        if (this.usersRepository.existsByUsername(userDto.getUsername()))
            throw new NonUniqueValueException("user", "username", userDto.getUsername());
        if (!this.rolesRepository.existsById(userDto.getRoleId()))
            // role must exist in the db
            throw new ResourceNotFoundException("role", "id", userDto.getRoleId());
        User user = User.from(userDto);
        User createdUser = this.usersRepository.save(user);
        return UserDto.from(createdUser);
    }

    public UserDto updateById(Integer id, UserDto updatesDto) {
        User user = this.usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        if (updatesDto.getUsername() != null && !updatesDto.getUsername().equals(user.getUsername())) {
            boolean usernameExists = this.usersRepository.existsByUsername(updatesDto.getUsername());
            if (usernameExists) {
                throw new NonUniqueValueException("user", "username", updatesDto.getUsername());
            }
            user.setUsername(updatesDto.getUsername());
        }
        if (updatesDto.getPassword() != null) user.setPassword(updatesDto.getPassword());
        if (updatesDto.getRoleId() != null) {
            Role role = this.rolesRepository.findById(updatesDto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("role", "id", updatesDto.getRoleId()));
            user.setRole(role);
        }
        User updatedUser = this.usersRepository.save(user);
        return UserDto.from(updatedUser);
    }

    public List<UserDto> deleteAll() {
        List<User> users = this.usersRepository.findAll();
        this.usersRepository.deleteAll();
        return UserDto.from(users);
    }

    public UserDto deleteById(Integer id) {
        User user = this.usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        this.usersRepository.deleteById(id);
        return UserDto.from(user);
    }
}
