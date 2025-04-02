package org.green_building.excellent_training.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<User> getAll() {
        return this.usersRepository.findAll();
    }

    public User getById(Integer id) {
        // findById(id) returns Optional<Role>
        return this.usersRepository.findById(id).orElse(null);
    }

    public User create(User user) {
        if (user.getUsername() == null) return null;
        if (user.getPassword() == null) return null;
        if (user.getRole() == null) return null;
        if (user.getRole().getId() == null) return null; // role id must be set
        if (this.rolesRepository.findById(user.getRole().getId()).isEmpty()) return null; // role must exist in the db
        return this.usersRepository.save(user);
    }

    public User updateById(Integer id, User updates) {
        User user = this.getById(id);
        if (user == null) return null;
        if (updates.getUsername() != null) user.setUsername(updates.getUsername());
        if (updates.getPassword() != null) user.setPassword(updates.getPassword());
        if (updates.getRole() != null) {
            if (updates.getRole().getId() == null || this.rolesRepository.findById(updates.getRole().getId()).isEmpty()) {
                return null; // role must exist in the db
            }
            user.setRole(updates.getRole());
        }
        return this.usersRepository.save(user);
    }

    public List<User> deleteAll() {
        List<User> users = this.usersRepository.findAll();
        this.usersRepository.deleteAll();
        return users;
    }

    public User deleteById(Integer id) {
        User user = this.getById(id); // if it doesn't exist, then this will be null
        this.usersRepository.deleteById(id);
        return user;
    }
}
