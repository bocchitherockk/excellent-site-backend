package org.green_building.excellent_training.repositories;

import java.util.Optional;

import org.green_building.excellent_training.entities.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StructuresRepository extends JpaRepository<Structure, Integer> {
    boolean existsByName(String name);
    boolean existsById(Integer id);
    Optional<Structure> findByName(String name);
}
