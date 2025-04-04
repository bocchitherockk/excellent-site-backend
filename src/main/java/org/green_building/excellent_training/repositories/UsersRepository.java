package org.green_building.excellent_training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.green_building.excellent_training.entities.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
}
