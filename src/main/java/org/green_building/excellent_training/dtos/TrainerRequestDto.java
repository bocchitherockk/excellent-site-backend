package org.green_building.excellent_training.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.green_building.excellent_training.entities.Trainer;

import jakarta.validation.constraints.Email;
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
public class TrainerRequestDto {

    @NotBlank
    @Email
    private String email;

    @JsonProperty("first_name")
    @NotBlank
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank
    private String lastName;

    @JsonProperty("phone_number")
    private Integer phoneNumber;

    @NotNull
    private Trainer.Type type;

    @JsonProperty("employer_id")
    @NotNull
    private Integer employerId;
}
