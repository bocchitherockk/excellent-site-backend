package org.green_building.excellent_training.repositories;

import java.util.Optional;

import org.green_building.excellent_training.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilesRepository extends JpaRepository<Profile, Integer> {
    boolean existsByName(String name);
    boolean existsById(Integer id);
    Optional<Profile> findByName(String name);
}
