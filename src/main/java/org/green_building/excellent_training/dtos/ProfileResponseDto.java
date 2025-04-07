package org.green_building.excellent_training.dtos;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.entities.Profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ProfileResponseDto {
    private Integer id;
    private String name;

    public ProfileResponseDto(String name) {
        this.name = name;
    }

    public static ProfileResponseDto from(Profile profile) {
        if (profile == null) return null;
        return ProfileResponseDto
            .builder()
            .id(profile.getId())
            .name(profile.getName())
            .build();
    }

    public static List<ProfileResponseDto> from(List<Profile> profiles) {
        if (profiles == null) return null;
        return profiles
            .stream()
            .map(profile -> ProfileResponseDto.from(profile))
            .collect(Collectors.toList());
    }
}
