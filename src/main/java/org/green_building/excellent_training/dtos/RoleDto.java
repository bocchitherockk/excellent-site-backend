package org.green_building.excellent_training.dtos;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.green_building.excellent_training.entities.Role;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RoleDto {
    private Integer id;
    private String name;

    public RoleDto(String name) {
        this.name = name;
    }

    // dto
    public static RoleDto from(Role role) {
        if (role == null) return null;
        return RoleDto.builder()
            .id(role.getId())
            .name(role.getName())
            .build();
    }

    public static List<RoleDto> from(List<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
            .map(role -> RoleDto.from(role))
            .collect(Collectors.toList());
    }
}
