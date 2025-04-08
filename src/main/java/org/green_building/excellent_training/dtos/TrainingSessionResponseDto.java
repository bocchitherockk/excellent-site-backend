package org.green_building.excellent_training.dtos;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.entities.TrainingSession;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class TrainingSessionResponseDto {

    private Integer id;
    private String title;
    private Integer year;
    @JsonProperty("duration_days")
    private Integer durationDays;
    private Double budget;
    @JsonProperty("domain_id")
    private Integer domainId;

    public TrainingSessionResponseDto(String title, Integer year, Integer durationDays, Double budget, Integer domainId) {
        this.title = title;
        this.year = year;
        this.durationDays = durationDays;
        this.budget = budget;
        this.domainId = domainId;
     }

    // dto
    public static TrainingSessionResponseDto from(TrainingSession trainingSession) {
        if (trainingSession == null) return null;
        return TrainingSessionResponseDto.builder()
            .id(trainingSession.getId())
            .title(trainingSession.getTitle())
            .year(trainingSession.getYear())
            .durationDays(trainingSession.getDurationDays())
            .budget(trainingSession.getBudget())
            .domainId(trainingSession.getDomain().getId())
            .build();
    }

    public static List<TrainingSessionResponseDto> from(List<TrainingSession> trainingSessions) {
        if (trainingSessions == null) return null;
        return trainingSessions.stream()
            .map(trainingSession -> TrainingSessionResponseDto.from(trainingSession))
            .collect(Collectors.toList());
    }
}
