package org.green_building.excellent_training.dtos;

import java.util.List;
import java.util.stream.Collectors;
import org.green_building.excellent_training.entities.Role;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RoleResponseDto {
    private Integer id;
    private String name;

    public RoleResponseDto(String name) {
        this.name = name;
    }

    public static RoleResponseDto from(Role role) {
        if (role == null) return null;
        return RoleResponseDto
            .builder()
            .name(role.getName())
            .build();
    }

    public static List<RoleResponseDto> from(List<Role> roles) {
        if (roles == null) return null;
        return roles
            .stream()
            .map(role -> RoleResponseDto.from(role))
            .collect(Collectors.toList());
    }
}
