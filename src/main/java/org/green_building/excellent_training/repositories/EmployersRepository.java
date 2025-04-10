package org.green_building.excellent_training.repositories;

import java.util.Optional;

import org.green_building.excellent_training.entities.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployersRepository extends JpaRepository<Employer, Integer> {
    boolean existsByName(String name);
    boolean existsById(Integer id);
    Optional<Employer> findByName(String name);
}
