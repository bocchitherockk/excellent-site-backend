package org.green_building.excellent_training.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class TrainingSessionRequestDto {

    @NotBlank
    private String title;

    @NotNull
    private Integer year;

    @JsonProperty("duration_days")
    @NotNull
    private Integer durationDays;

    @NotNull
    private Double budget;

    @JsonProperty("domain_id")
    @NotNull
    private Integer domainId;
}
