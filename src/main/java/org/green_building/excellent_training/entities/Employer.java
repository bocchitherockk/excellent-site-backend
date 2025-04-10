package org.green_building.excellent_training.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.EmployerRequestDto;

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
@Entity(name = "Employer")
@Table(name = "employers")
public class Employer {

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

    /*
    @OneToMany (
        mappedBy = "employer",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )

    @JsonManagedReference
    private List<Trainer> trainers;
    */

    // a constructor that does not have the field 'id' because it is auto generated
    public Employer(String name) {
        this.name = name;
    }

    // request dto
    public static Employer from(EmployerRequestDto dto) {
        if (dto == null) return null;
        return Employer.builder()
            .name(dto.getName())
            .build();
    }

    public static List<Employer> from(List<EmployerRequestDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(dto -> Employer.from(dto))
            .collect(Collectors.toList());
    }
}
