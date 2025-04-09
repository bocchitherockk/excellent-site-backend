package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.ParticipantRequestDto;
import org.green_building.excellent_training.dtos.ParticipantResponseDto;
import org.green_building.excellent_training.dtos.TrainingSessionResponseDto;
import org.green_building.excellent_training.services.ParticipantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/participants")
// @CrossOrigin(origins = "http://localhost:6969")
public class ParticipantsController {

    private final ParticipantsService participantsService;

    @Autowired
    public ParticipantsController(ParticipantsService participantsService) {
        this.participantsService = participantsService;
    }

    @GetMapping({ "",  "/" })
    public ResponseEntity<Map<String, List<ParticipantResponseDto>>> get() {
        List<ParticipantResponseDto> participants = this.participantsService.getAll();
        Map<String, List<ParticipantResponseDto>> responseBody = new HashMap<>();
        responseBody.put("participants", participants);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, ParticipantResponseDto>> get(@PathVariable Integer id) {
        ParticipantResponseDto participant = this.participantsService.getById(id);
        Map<String, ParticipantResponseDto> responseBody = new HashMap<>();
        responseBody.put("participant", participant);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{participantId}/training_sessions", "/{participantId}/training_sessions/" })
    public ResponseEntity<Map<String, List<TrainingSessionResponseDto>>> getTrainingSessions(@PathVariable Integer participantId) {
        List<TrainingSessionResponseDto> trainingSessions = this.participantsService.getParticipantTrainingSessionsById(participantId);
        Map<String, List<TrainingSessionResponseDto>> responseBody = new HashMap<>();
        responseBody.put("training_sessions", trainingSessions);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{participantId}/training_sessions/{trainingSessionId}", "/{participantId}/training_sessions/{trainingSessionId}/" })
    public ResponseEntity<Map<String, TrainingSessionResponseDto>> getTrainingSessions(@PathVariable Integer participantId, @PathVariable Integer trainingSessionId) {
        TrainingSessionResponseDto trainingSession = this.participantsService.getParticipantTrainingSessionById(participantId, trainingSessionId);
        Map<String, TrainingSessionResponseDto> responseBody = new HashMap<>();
        responseBody.put("training_session", trainingSession);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, ParticipantResponseDto>> post(@Valid @RequestBody ParticipantRequestDto request) {
        ParticipantResponseDto createdParticipant = this.participantsService.create(request);
        Map<String, ParticipantResponseDto> responseBody = new HashMap<>();
        responseBody.put("created_participant", createdParticipant);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping({ "/{participantId}/training_sessions", "/{participantId}/training_sessions/" })
    public ResponseEntity<Map<String, List<TrainingSessionResponseDto>>> post(@PathVariable Integer participantId, @RequestBody Map<String, List<Integer>> requestBody) {
        List<TrainingSessionResponseDto> enrolledInTrainingSessions = this.participantsService.enrollParticipantInTrainingSessions(participantId, requestBody.get("training_sessions_ids"));
        Map<String, List<TrainingSessionResponseDto>> responseBody = new HashMap<>();
        responseBody.put("enrolled_in_training_sessions", enrolledInTrainingSessions);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "/{participantId}/training_sessions/{trainingSessionId}", "/{participantId}/training_sessions/{trainingSessionId}/" })
    public ResponseEntity<Map<String, TrainingSessionResponseDto>> post(@PathVariable Integer participantId, @PathVariable Integer trainingSessionId) {
        TrainingSessionResponseDto enrolledInTrainingSession = this.participantsService.enrollParticipantInTrainingSession(participantId, trainingSessionId);
        Map<String, TrainingSessionResponseDto> responseBody = new HashMap<>();
        responseBody.put("enrolled_in_training_session", enrolledInTrainingSession);
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, ParticipantResponseDto>> put(@PathVariable Integer id, @RequestBody ParticipantRequestDto updates) {
        ParticipantResponseDto updatedParticipant = this.participantsService.updateById(id, updates);
        Map<String, ParticipantResponseDto> responseBody = new HashMap<>();
        responseBody.put("updated_participant", updatedParticipant);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<ParticipantResponseDto>>> delete() {
        List<ParticipantResponseDto> deletedParticipants = this.participantsService.deleteAll();
        Map<String, List<ParticipantResponseDto>> responseBody = new HashMap<>();
        responseBody.put("deleted_participants", deletedParticipants);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, ParticipantResponseDto>> delete(@PathVariable Integer id) {
        ParticipantResponseDto deletedParticipant = this.participantsService.deleteById(id);
        Map<String, ParticipantResponseDto> responseBody = new HashMap<>();
        responseBody.put("deleted_participant", deletedParticipant);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{participantId}/training_sessions", "/{participantId}/training_sessions/" })
    public ResponseEntity<Map<String, List<TrainingSessionResponseDto>>> cancelParticipantFromTrainingSessions(@PathVariable Integer participantId) {
        List<TrainingSessionResponseDto> canceledFromTrainingSessions = this.participantsService.cancelParticipantFromTrainingSessions(participantId);
        Map<String, List<TrainingSessionResponseDto>> responseBody = new HashMap<>();
        responseBody.put("canceled_from_training_sessions", canceledFromTrainingSessions);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{participantId}/training_sessions/{trainingSessionId}", "/{participantId}/training_sessions/{trainingSessionId}/" })
    public ResponseEntity<Map<String, TrainingSessionResponseDto>> cancelParticipantFromTrainingSession(@PathVariable Integer participantId, @PathVariable Integer trainingSessionId) {
        TrainingSessionResponseDto canceledFromTrainingSession = this.participantsService.cancelParticipantFromTrainingSession(participantId, trainingSessionId);
        Map<String, TrainingSessionResponseDto> responseBody = new HashMap<>();
        responseBody.put("canceled_from_training_session", canceledFromTrainingSession);
        return ResponseEntity.ok(responseBody);
    }
}
