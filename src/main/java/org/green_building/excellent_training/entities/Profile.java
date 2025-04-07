package org.green_building.excellent_training.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.ProfileRequestDto;

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
@Entity(name = "Profile")
@Table(name = "profiles")
public class Profile {

    public static final String ENGINEER_3 = "ENGINEER+3";
    public static final String ENGINEER_5 = "ENGINEER+5";
    public static final String TECHNICIAN = "TECHNICIAN";
    public static final String LAWYER     = "LAWYER";

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
        mappedBy = "profile",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonManagedReference
    private List<User> users;

    // a constructor that does not have the field 'id' because it is auto generated
    public Profile(String name) {
        this.name = name;
    }

    // request dto
    public static Profile from(ProfileRequestDto dto) {
        if (dto == null) return null;
        return Profile.builder()
            .name(dto.getName())
            .build();
    }

    public static List<Profile> from(List<ProfileRequestDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(dto -> Profile.from(dto))
            .collect(Collectors.toList());
    }
}
