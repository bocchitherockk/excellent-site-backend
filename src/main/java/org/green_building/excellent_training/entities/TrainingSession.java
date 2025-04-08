package org.green_building.excellent_training.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.TrainingSessionRequestDto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "training_sessions")
public class TrainingSession {

    /********************* id *********************/
    @Id
    @GeneratedValue
    @Column (
        name = "id",
        unique = true,
        nullable = false,
        insertable = false,
        updatable = false
    )
    private Integer id;

    /********************* title *********************/
    @Column (
        length = 100,
        unique = false, // TODO: should the title be unique ???
        nullable = false
    )
    private String title;

    /********************* year *********************/
    @Column (nullable = false)
    private Integer year;

    /********************* duration_days *********************/
    @Column (
        name = "duration_days",
        nullable = false
    )
    private Integer durationDays;

    /********************* budget *********************/
    @Column (nullable = false)
    private Double budget;

    /********************* domain *********************/
    @ManyToOne
    @JoinColumn (
        name = "domain_id",
        nullable = false
    )
    @JsonBackReference
    private Domain domain;

    // a constructor that does not have the field 'id' because it is auto generated
    public TrainingSession(String title, Integer year, Integer durationDays, Double budget, Domain domain) {
        this.title = title;
        this.year = year;
        this.durationDays = durationDays;
        this.budget = budget;
        this.domain = domain;
     }

    // dto
    public static TrainingSession from(TrainingSessionRequestDto dto) {
        if (dto == null) return null;
        /* in case we are building a trainingSession out of put request modifications */
        Domain domain = dto.getDomainId() == null ? null : Domain.builder().id(dto.getDomainId()).build();
        return TrainingSession.builder()
            .title(dto.getTitle())
            .year(dto.getYear())
            .durationDays(dto.getDurationDays())
            .budget(dto.getBudget())
            .domain(domain)
            .build();
    }

    public static List<TrainingSession> from(List<TrainingSessionRequestDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(dto -> TrainingSession.from(dto))
            .collect(Collectors.toList());
    }
}
