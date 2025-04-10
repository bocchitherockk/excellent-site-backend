package org.green_building.excellent_training.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.TrainerRequestDto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
@Table(name = "trainers")
public class Trainer {
    public enum Type {
        INTERNAL,
        EXTERNAL;
    }

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

    /********************* email *********************/
    @Column (
        name = "email",
        length = 100,
        unique = true,
        nullable = false
    )
    private String email;

    /********************* first_name *********************/
    @Column (
        name = "first_name",
        length = 100,
        nullable = false
    )
    private String firstName;

    /********************* last_name *********************/
    @Column (
        name = "last_name",
        length = 100,
        nullable = false
    )
    private String lastName;

    /********************* phone_number *********************/
    @Column (name = "phone_number")
    private Integer phoneNumber;

    /********************* type *********************/
    @Enumerated (EnumType.STRING)
    private Trainer.Type type;

    /********************* employer *********************/
    @ManyToOne
    @JoinColumn (
        name = "employer_id",
        nullable = false
    )
    @JsonBackReference
    private Employer employer;

    /********************* training_sessions *********************/
    /*
    @ManyToMany (
        mappedBy = "trainers"
    )
    private List<TrainingSession> trainingSessions;
    */

    // a constructor that does not have the field 'id' because it is auto generated
    public Trainer(String email, String firstName, String lastName, Integer phoneNumber, Trainer.Type type, Employer employer) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.employer = employer;
    }

    // dto
    public static Trainer from(TrainerRequestDto dto) {
        if (dto == null) return null;
        /* in case we are building a trainer out of put request modifications */
        Employer employer = dto.getEmployerId() == null ? null : Employer.builder().id(dto.getEmployerId()).build();
        return Trainer.builder()
            .email(dto.getEmail())
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .phoneNumber(dto.getPhoneNumber())
            .type(dto.getType())
            .employer(employer)
            .build();
    }

    public static List<Trainer> from(List<TrainerRequestDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(dto -> Trainer.from(dto))
            .collect(Collectors.toList());
    }
}
