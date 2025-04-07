package org.green_building.excellent_training.dtos;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.entities.Structure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class StructureResponseDto {
    private Integer id;
    private String name;

    public StructureResponseDto(String name) {
        this.name = name;
    }

    public static StructureResponseDto from(Structure structure) {
        if (structure == null) return null;
        return StructureResponseDto
            .builder()
            .id(structure.getId())
            .name(structure.getName())
            .build();
    }

    public static List<StructureResponseDto> from(List<Structure> structures) {
        if (structures == null) return null;
        return structures
            .stream()
            .map(structure -> StructureResponseDto.from(structure))
            .collect(Collectors.toList());
    }
}
