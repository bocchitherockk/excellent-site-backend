package org.green_building.excellent_training.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.green_building.excellent_training.dtos.UserDto;
import org.green_building.excellent_training.entities.User;
import org.green_building.excellent_training.repositories.RolesRepository;
import org.green_building.excellent_training.repositories.UsersRepository;

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
        // findById(id) returns Optional<Role>
        User user = this.usersRepository.findById(id).orElse(null);
        return UserDto.from(user);
    }

    public UserDto create(UserDto userDto) {
        if (userDto.getUsername() == null) return null;
        if (userDto.getPassword() == null) return null;
        if (userDto.getRoleId()   == null) return null; // role id must be set
        if (this.rolesRepository.findById(userDto.getRoleId()).isEmpty()) return null; // role must exist in the db
        User user = User.from(userDto);
        User createdUser = this.usersRepository.save(user);
        return UserDto.from(createdUser);
    }

    public UserDto updateById(Integer id, UserDto updatesDto) {
        User updates = User.from(updatesDto);
        User user = this.usersRepository.findById(id).orElse(null);
        if (user == null) return null;
        if (updates.getUsername() != null) user.setUsername(updates.getUsername());
        if (updates.getPassword() != null) user.setPassword(updates.getPassword());
        if (updates.getRole() != null) {
            if (updates.getRole().getId() == null || this.rolesRepository.findById(updates.getRole().getId()).isEmpty()) {
                return null; // role must exist in the db
            }
            user.setRole(updates.getRole());
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
        UserDto userDto = this.getById(id); // if it doesn't exist, then this will be null
        if (userDto == null) return null;
        this.usersRepository.deleteById(id);
        return userDto;
    }
}
