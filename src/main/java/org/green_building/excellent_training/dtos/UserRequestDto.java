package org.green_building.excellent_training.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @JsonProperty("old_password")
    private String oldPassword;

    // if not specified, pick role "USER" by default
    @JsonProperty("role_id") // receive role_id in the json request body
    private Integer roleId;
}
