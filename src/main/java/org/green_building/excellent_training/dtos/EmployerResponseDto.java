package org.green_building.excellent_training.dtos;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.entities.Employer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmployerResponseDto {
    private Integer id;
    private String name;

    public EmployerResponseDto(String name) {
        this.name = name;
    }

    public static EmployerResponseDto from(Employer employer) {
        if (employer == null) return null;
        return EmployerResponseDto
            .builder()
            .id(employer.getId())
            .name(employer.getName())
            .build();
    }

    public static List<EmployerResponseDto> from(List<Employer> employers) {
        if (employers == null) return null;
        return employers
            .stream()
            .map(employer -> EmployerResponseDto.from(employer))
            .collect(Collectors.toList());
    }
}
