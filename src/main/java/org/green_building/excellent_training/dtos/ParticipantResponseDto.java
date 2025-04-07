package org.green_building.excellent_training.dtos;

import java.util.List;
import java.util.stream.Collectors;

import org.green_building.excellent_training.entities.Participant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ParticipantResponseDto {

    private Integer id;
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("phone_number")
    private Integer phoneNumber;
    @JsonProperty("profile_id")
    private Integer profileId;
    @JsonProperty("structure_id")
    private Integer structureId;

    public ParticipantResponseDto(String email, String firstName, String lastName, Integer phoneNumber, Integer profileId, Integer structureId) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.profileId = profileId;
        this.structureId = structureId;
    }

    // dto
    public static ParticipantResponseDto from(Participant participant) {
        if (participant == null) return null;
        return ParticipantResponseDto.builder()
            .id(participant.getId())
            .email(participant.getEmail())
            .firstName(participant.getFirstName())
            .lastName(participant.getLastName())
            .phoneNumber(participant.getPhoneNumber())
            .profileId(participant.getProfile().getId())
            .structureId(participant.getStructure().getId())
            .build();
    }

    public static List<ParticipantResponseDto> from(List<Participant> participants) {
        if (participants == null) return null;
        return participants.stream()
            .map(dto -> ParticipantResponseDto.from(dto))
            .collect(Collectors.toList());
    }
}
