package org.green_building.excellent_training.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ParticipantRequestDto {

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

    @JsonProperty("profile_id")
    @NotNull
    private Integer profileId;

    @JsonProperty("structure_id")
    @NotNull
    private Integer structureId;
}
