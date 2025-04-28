package org.green_building.excellent_training.repositories;

import org.green_building.excellent_training.entities.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingSessionsRepository extends JpaRepository<TrainingSession, Integer> {
    /* note: this is in case you want to make the title unique
    boolean existsByTitle(String title);
    */

    @Query("SELECT COALESCE(SUM(ts.budget), 0.0) FROM TrainingSession ts " +
        "WHERE ts.domain.id = :domainId AND ts.year = :year")
    Double sumBudgetByDomainAndYear(@Param("domainId") Integer domainId, @Param("year") Integer year);
}
