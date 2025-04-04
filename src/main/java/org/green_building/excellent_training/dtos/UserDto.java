package org.green_building.excellent_training.dtos;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.green_building.excellent_training.entities.User;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserDto {

    private Integer id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @JsonProperty("role_id") // receive role_id in the json request body
    @Min(1)
    private Integer roleId;

    public UserDto(String username, String password, Integer roleId) {
        this.username = username;
        this.password = password;
        this.roleId = roleId;
    }

    // dto
    public static UserDto from(User user) {
        if (user == null) return null;
        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .roleId(user.getRole().getId())
            .build();
    }

    public static List<UserDto> from(List<User> users) {
        if (users == null) return null;
        return users.stream()
            .map(user -> UserDto.from(user))
            .collect(Collectors.toList());
    }
}
