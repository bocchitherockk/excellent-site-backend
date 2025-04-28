package org.green_building.excellent_training.dtos;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.entities.Trainer;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class TrainerResponseDto {

    private Integer id;
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("phone_number")
    private Integer phoneNumber;
    private Trainer.Type type;
    @JsonProperty("employer_id")
    private Integer employerId;

    public TrainerResponseDto(String email, String firstName, String lastName, Integer phoneNumber, Trainer.Type trainerType, Integer employerId) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.type = trainerType;
        this.employerId = employerId;
    }

    // dto
    public static TrainerResponseDto from(Trainer trainer) {
        if (trainer == null) return null;
        return TrainerResponseDto.builder()
            .id(trainer.getId())
            .email(trainer.getEmail())
            .firstName(trainer.getFirstName())
            .lastName(trainer.getLastName())
            .phoneNumber(trainer.getPhoneNumber())
            .type(trainer.getType())
            .employerId(trainer.getEmployer().getId())
            .build();
    }

    public static List<TrainerResponseDto> from(List<Trainer> trainers) {
        if (trainers == null) return null;
        return trainers.stream()
            .map(dto -> TrainerResponseDto.from(dto))
            .collect(Collectors.toList());
    }
}
