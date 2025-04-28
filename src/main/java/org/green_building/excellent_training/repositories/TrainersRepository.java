package org.green_building.excellent_training.repositories;

import org.green_building.excellent_training.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainersRepository extends JpaRepository<Trainer, Integer> {
    boolean existsByEmail(String email);
    Integer countByEmployerId(Integer employerId);
}
