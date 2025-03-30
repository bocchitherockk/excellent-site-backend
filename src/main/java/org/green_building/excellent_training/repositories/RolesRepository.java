package org.green_building.excellent_training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.green_building.excellent_training.entities.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {}
