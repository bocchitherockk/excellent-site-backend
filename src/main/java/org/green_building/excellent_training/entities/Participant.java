package org.green_building.excellent_training.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.dtos.ParticipantRequestDto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "participants")
public class Participant {

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

    /********************* profile *********************/
    @ManyToOne
    @JoinColumn (
        name = "profile_id",
        nullable = false
    )
    @JsonBackReference
    private Profile profile;

    /********************* structure *********************/
    @ManyToOne
    @JoinColumn (
        name = "structure_id",
        nullable = false
    )
    @JsonBackReference
    private Structure structure;

    /********************* training_sessions *********************/
    @ManyToMany (
        mappedBy = "participants"
    )
    private List<TrainingSession> trainingSessions;

    // a constructor that does not have the field 'id' because it is auto generated
    public Participant(String email, String firstName, String lastName, Integer phoneNumber, Profile profile, Structure structure) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
        this.structure = structure;
    }

    // dto
    public static Participant from(ParticipantRequestDto dto) {
        if (dto == null) return null;
        /* in case we are building a participant out of put request modifications */
        Profile profile = dto.getProfileId() == null ? null : Profile.builder().id(dto.getProfileId()).build();
        Structure structure = dto.getStructureId() == null ? null : Structure.builder().id(dto.getStructureId()).build();
        return Participant.builder()
            .email(dto.getEmail())
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .phoneNumber(dto.getPhoneNumber())
            .profile(profile)
            .structure(structure)
            .build();
    }

    public static List<Participant> from(List<ParticipantRequestDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(dto -> Participant.from(dto))
            .collect(Collectors.toList());
    }
}
