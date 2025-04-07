package org.green_building.excellent_training.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.UserRequestDto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

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

    /********************* username *********************/
    @Column (
        name = "username",
        length = 100,
        unique = true,
        nullable = false
    )
    private String username;

    /********************* password *********************/
    @Column (
        name = "password",
        length = 255,
        nullable = false // maybe in the future we will add something like oauth2 authentication so it will become nullable
    )
    private String password;

    /********************* role *********************/
    @ManyToOne
    @JoinColumn (
        name = "role_id", // the name of the foreign key column in the database
        nullable = false
    )
    @JsonBackReference
    private Role role;


    // a constructor that does not have the field 'id' because it is auto generated
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // dto
    public static User from(UserRequestDto dto) {
        if (dto == null) return null;
        /* in case we are building a user out of put request modifications */
        Role role = dto.getRoleId() == null ? null : Role.builder().id(dto.getRoleId()).build();
        return User.builder()
            .username(dto.getUsername())
            .password(dto.getPassword())
            .role(role)
            .build();
    }

    public static List<User> from(List<UserRequestDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(dto -> User.from(dto))
            .collect(Collectors.toList());
    }
}
