package org.green_building.excellent_training.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.StructureRequestDto;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity(name = "Structure")
@Table(name = "structures")
public class Structure {

    /********************* id *********************/
    @Id
    @GeneratedValue
    @Column (
        name = "id",
        unique = true,
        nullable = false,
        insertable = false,
        updatable = false
    )
    private Integer id;

    /********************* name *********************/
    @Column (
        name = "name",
        length = 50,
        unique = true,
        nullable = false
    )
    private String name;

    @OneToMany (
        mappedBy = "structure"
    )
    @JsonManagedReference
    private List<Participant> participants;

    // a constructor that does not have the field 'id' because it is auto generated
    public Structure(String name) {
        this.name = name;
    }

    // request dto
    public static Structure from(StructureRequestDto dto) {
        if (dto == null) return null;
        return Structure.builder()
            .name(dto.getName())
            .participants(new ArrayList<>())
            .build();
    }

    public static List<Structure> from(List<StructureRequestDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(dto -> Structure.from(dto))
            .collect(Collectors.toList());
    }
}
