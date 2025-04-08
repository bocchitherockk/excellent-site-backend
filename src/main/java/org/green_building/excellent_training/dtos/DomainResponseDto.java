package org.green_building.excellent_training.dtos;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.entities.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class DomainResponseDto {
    private Integer id;
    private String name;

    public DomainResponseDto(String name) {
        this.name = name;
    }

    public static DomainResponseDto from(Domain domain) {
        if (domain == null) return null;
        return DomainResponseDto
            .builder()
            .id(domain.getId())
            .name(domain.getName())
            .build();
    }

    public static List<DomainResponseDto> from(List<Domain> domains) {
        if (domains == null) return null;
        return domains
            .stream()
            .map(domain -> DomainResponseDto.from(domain))
            .collect(Collectors.toList());
    }
}
