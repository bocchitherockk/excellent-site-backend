package org.green_building.excellent_training.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.RoleDto;

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
@NoArgsConstructor // this is needed from when we read from the database we need to create an empty object to store the values in it
@Entity(name = "Role")
@Table(name = "roles")
public class Role {

    public static final String USER        = "USER";
    public static final String RESPONSIBLE = "RESPONSIBLE";
    public static final String ADMIN       = "ADMIN";

    /********************* id *********************/
    @Id
    /*
    @SequenceGenerator (
        name = "roles_sequence",
        sequenceName = "roles_id_seq",
        allocationSize = 1,
        initialValue = 1
    )
    */
    @GeneratedValue/* (
        strategy = GenerationType.SEQUENCE,
        generator = "roles_id_seq"
                     )
    */
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
        length = 50, // meaning it is a varchar(50), varchar(255) is the default // it is the same as setting columnDefinition = "varchar(50)"
        unique = true,
        nullable = false
    )
    private String name;

    @OneToMany (
        mappedBy = "role", // this is the field in the User class that maps to this Role // this is required in order to avoid creating a relationship table in the database
        cascade = CascadeType.ALL, // CascadeTypeType.ALL means that all operations (persist, merge, remove, refresh, detach) will be cascaded to the users
        orphanRemoval = true // removing the user from the List in java will remove it from the database
    )
    @JsonManagedReference // this tells jackson that the parent is in charge of serializing the child and prevents the child from serializing the parent // is used to avoid circular references when serializing the object to JSON
    private List<User> users;

    // a constructor that does not have the field 'id' because it is auto generated
    public Role(String name) {
        this.name = name;
    }

    // dto
    public static Role from(RoleDto roleDto) {
        if (roleDto == null) return null;
        return Role.builder()
            .id(roleDto.getId())
            .name(roleDto.getName())
            .build();
    }

    public static List<Role> from(List<RoleDto> rolesDto) {
        if (rolesDto == null) return null;
        return rolesDto.stream()
            .map(roleDto -> Role.from(roleDto))
            .collect(Collectors.toList());
    }
}
