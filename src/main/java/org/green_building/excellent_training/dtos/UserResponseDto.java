package org.green_building.excellent_training.dtos;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.entities.User;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserResponseDto {

    private Integer id;
    private String username;
    @JsonProperty("role_id") // send role_id in the json response body
    private Integer roleId;

    public UserResponseDto(String username, Integer roleId) {
        this.username = username;
        this.roleId = roleId;
    }

    // dto
    public static UserResponseDto from(User user) {
        if (user == null) return null;
        return UserResponseDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .roleId(user.getRole().getId())
            .build();
    }

    public static List<UserResponseDto> from(List<User> users) {
        if (users == null) return null;
        return users.stream()
            .map(user -> UserResponseDto.from(user))
            .collect(Collectors.toList());
    }
}
