package org.green_building.excellent_training.repositories;

import org.green_building.excellent_training.entities.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingSessionsRepository extends JpaRepository<TrainingSession, Integer> {
    /* note: this is in case you want to make the title unique
    boolean existsByTitle(String title);
    */
}
