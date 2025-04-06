package org.green_building.excellent_training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.green_building.excellent_training.entities.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {
    boolean existsByName(String name);
    boolean existsById(Integer id);
    Optional<Role> findByName(String name);
}
