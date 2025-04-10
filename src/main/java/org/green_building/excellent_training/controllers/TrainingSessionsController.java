package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.ParticipantResponseDto;
import org.green_building.excellent_training.dtos.TrainerResponseDto;
import org.green_building.excellent_training.dtos.TrainingSessionRequestDto;
import org.green_building.excellent_training.dtos.TrainingSessionResponseDto;
import org.green_building.excellent_training.services.TrainingSessionsService;
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
@RequestMapping("/training_sessions")
// @CrossOrigin(origins = "http://localhost:6969")
public class TrainingSessionsController {

    private final TrainingSessionsService trainingSessionsService;

    @Autowired
    public TrainingSessionsController(TrainingSessionsService trainingSessionsService) {
        this.trainingSessionsService = trainingSessionsService;
    }

    @GetMapping({ "",  "/" })
    public ResponseEntity<Map<String, List<TrainingSessionResponseDto>>> get() {
        List<TrainingSessionResponseDto> trainingSessions = this.trainingSessionsService.getAll();
        Map<String, List<TrainingSessionResponseDto>> responseBody = new HashMap<>();
        responseBody.put("training_sessions", trainingSessions);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, TrainingSessionResponseDto>> get(@PathVariable Integer id) {
        TrainingSessionResponseDto trainingSession = this.trainingSessionsService.getById(id);
        Map<String, TrainingSessionResponseDto> responseBody = new HashMap<>();
        responseBody.put("training_session", trainingSession);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{trainingSessionId}/participants", "/{trainingSessionId}/participants/" })
    public ResponseEntity<Map<String, List<ParticipantResponseDto>>> getParticipants(@PathVariable Integer trainingSessionId) {
        List<ParticipantResponseDto> participants = this.trainingSessionsService.getTrainingSessionParticipantsById(trainingSessionId);
        Map<String, List<ParticipantResponseDto>> responseBody = new HashMap<>();
        responseBody.put("participants", participants);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{trainingSessionId}/trainers", "/{trainingSessionId}/trainers/" })
    public ResponseEntity<Map<String, List<TrainerResponseDto>>> getTrainers(@PathVariable Integer trainingSessionId) {
        List<TrainerResponseDto> trainers = this.trainingSessionsService.getTrainingSessionTrainersById(trainingSessionId);
        Map<String, List<TrainerResponseDto>> responseBody = new HashMap<>();
        responseBody.put("trainers", trainers);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{trainingSessionId}/participants/{participantId}", "/{trainingSessionId}/participants/{participantId}/" })
    public ResponseEntity<Map<String, ParticipantResponseDto>> getParticipant(@PathVariable Integer trainingSessionId, @PathVariable Integer participantId) {
        ParticipantResponseDto participant = this.trainingSessionsService.getTrainingSessionParticipantById(trainingSessionId, participantId);
        Map<String, ParticipantResponseDto> responseBody = new HashMap<>();
        responseBody.put("participant", participant);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{trainingSessionId}/trainers/{trainerId}", "/{trainingSessionId}/trainers/{trainerId}/" })
    public ResponseEntity<Map<String, TrainerResponseDto>> getTrainer(@PathVariable Integer trainingSessionId, @PathVariable Integer trainerId) {
        TrainerResponseDto trainer = this.trainingSessionsService.getTrainingSessionTrainerById(trainingSessionId, trainerId);
        Map<String, TrainerResponseDto> responseBody = new HashMap<>();
        responseBody.put("trainer", trainer);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, TrainingSessionResponseDto>> post(@Valid @RequestBody TrainingSessionRequestDto request) {
        TrainingSessionResponseDto createdTrainingSession = this.trainingSessionsService.create(request);
        Map<String, TrainingSessionResponseDto> responseBody = new HashMap<>();
        responseBody.put("created_training_session", createdTrainingSession);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping({ "/{trainingSessionId}/participants", "/{trainingSessionId}/participants/" })
    public ResponseEntity<Map<String, List<ParticipantResponseDto>>> postParticipants(@PathVariable Integer trainingSessionId, @RequestBody Map<String, List<Integer>> requestBody) {
        List<ParticipantResponseDto> enrolledInParticipants = this.trainingSessionsService.enrollParticipantsInTrainingSession(trainingSessionId, requestBody.get("participants_ids"));
        Map<String, List<ParticipantResponseDto>> responseBody = new HashMap<>();
        responseBody.put("enrolled_in_participants", enrolledInParticipants);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "/{trainingSessionId}/trainers", "/{trainingSessionId}/trainers/" })
    public ResponseEntity<Map<String, List<TrainerResponseDto>>> postTrainers(@PathVariable Integer trainingSessionId, @RequestBody Map<String, List<Integer>> requestBody) {
        List<TrainerResponseDto> assignedInTrainers = this.trainingSessionsService.assignTrainersToTrainingSession(trainingSessionId, requestBody.get("trainers_ids"));
        Map<String, List<TrainerResponseDto>> responseBody = new HashMap<>();
        responseBody.put("assigned_in_trainers", assignedInTrainers);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "/{trainingSessionId}/participants/{participantId}", "/{trainingSessionId}/participants/{participantId}/" })
    public ResponseEntity<Map<String, ParticipantResponseDto>> postParticipant(@PathVariable Integer trainingSessionId, @PathVariable Integer participantId) {
        ParticipantResponseDto enrolledInParticipant = this.trainingSessionsService.enrollParticipantInTrainingSession(trainingSessionId, participantId);
        Map<String, ParticipantResponseDto> responseBody = new HashMap<>();
        responseBody.put("enrolled_in_participant", enrolledInParticipant);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "/{trainingSessionId}/trainers/{trainerId}", "/{trainingSessionId}/trainers/{trainerId}/" })
    public ResponseEntity<Map<String, TrainerResponseDto>> postTrainer(@PathVariable Integer trainingSessionId, @PathVariable Integer trainerId) {
        TrainerResponseDto assignedInTrainer = this.trainingSessionsService.assignTrainerToTrainingSession(trainingSessionId, trainerId);
        Map<String, TrainerResponseDto> responseBody = new HashMap<>();
        responseBody.put("assigned_in_trainer", assignedInTrainer);
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, TrainingSessionResponseDto>> put(@PathVariable Integer id, @RequestBody TrainingSessionRequestDto updates) {
        TrainingSessionResponseDto updatedTrainingSession = this.trainingSessionsService.updateById(id, updates);
        Map<String, TrainingSessionResponseDto> responseBody = new HashMap<>();
        responseBody.put("updated_training_session", updatedTrainingSession);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<TrainingSessionResponseDto>>> delete() {
        List<TrainingSessionResponseDto> deletedTrainingSessions = this.trainingSessionsService.deleteAll();
        Map<String, List<TrainingSessionResponseDto>> responseBody = new HashMap<>();
        responseBody.put("deleted_training_sessions", deletedTrainingSessions);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, TrainingSessionResponseDto>> delete(@PathVariable Integer id) {
        TrainingSessionResponseDto deletedTrainingSession = this.trainingSessionsService.deleteById(id);
        Map<String, TrainingSessionResponseDto> responseBody = new HashMap<>();
        responseBody.put("deleted_training_session", deletedTrainingSession);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{trainingSessionId}/participants", "/{trainingSessionId}/participants/" })
    public ResponseEntity<Map<String, List<ParticipantResponseDto>>> cancelParticipants(@PathVariable Integer trainingSessionId) {
        List<ParticipantResponseDto> deletedParticipants = this.trainingSessionsService.cancelParticipantsFromTrainingSession(trainingSessionId);
        Map<String, List<ParticipantResponseDto>> responseBody = new HashMap<>();
        responseBody.put("canceled_participants", deletedParticipants);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{trainingSessionId}/trainers", "/{trainingSessionId}/trainers/" })
    public ResponseEntity<Map<String, List<TrainerResponseDto>>> cancelTrainers(@PathVariable Integer trainingSessionId) {
        List<TrainerResponseDto> deletedTrainers = this.trainingSessionsService.cancelTrainersFromTrainingSession(trainingSessionId);
        Map<String, List<TrainerResponseDto>> responseBody = new HashMap<>();
        responseBody.put("canceled_trainers", deletedTrainers);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{trainingSessionId}/participants/{participantId}", "/{trainingSessionId}/participants/{participantId}/" })
    public ResponseEntity<Map<String, ParticipantResponseDto>> cancelParticipant(@PathVariable Integer trainingSessionId, @PathVariable Integer participantId) {
        ParticipantResponseDto deletedParticipant = this.trainingSessionsService.cancelParticipantFromTrainingSession(trainingSessionId, participantId);
        Map<String, ParticipantResponseDto> responseBody = new HashMap<>();
        responseBody.put("canceled_participant", deletedParticipant);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{trainingSessionId}/trainers/{trainerId}", "/{trainingSessionId}/trainers/{trainerId}/" })
    public ResponseEntity<Map<String, TrainerResponseDto>> cancelTrainer(@PathVariable Integer trainingSessionId, @PathVariable Integer trainerId) {
        TrainerResponseDto deletedTrainer = this.trainingSessionsService.cancelTrainerFromTrainingSession(trainingSessionId, trainerId);
        Map<String, TrainerResponseDto> responseBody = new HashMap<>();
        responseBody.put("canceled_trainer", deletedTrainer);
        return ResponseEntity.ok(responseBody);
    }
}
