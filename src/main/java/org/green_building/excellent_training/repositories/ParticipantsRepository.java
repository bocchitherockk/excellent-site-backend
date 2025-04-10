package org.green_building.excellent_training.repositories;

import org.green_building.excellent_training.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantsRepository extends JpaRepository<Participant, Integer> {
    boolean existsByEmail(String email);
}
