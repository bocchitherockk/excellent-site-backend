package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.TrainerRequestDto;
import org.green_building.excellent_training.dtos.TrainerResponseDto;
import org.green_building.excellent_training.dtos.TrainingSessionResponseDto;
import org.green_building.excellent_training.services.TrainersService;
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
@RequestMapping("/trainers")
// @CrossOrigin(origins = "http://localhost:6969")
public class TrainersController {

    private final TrainersService trainersService;

    @Autowired
    public TrainersController(TrainersService trainersService) {
        this.trainersService = trainersService;
    }

    @GetMapping({ "",  "/" })
    public ResponseEntity<Map<String, List<TrainerResponseDto>>> get() {
        List<TrainerResponseDto> trainers = this.trainersService.getAll();
        Map<String, List<TrainerResponseDto>> responseBody = new HashMap<>();
        responseBody.put("trainers", trainers);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, TrainerResponseDto>> get(@PathVariable Integer id) {
        TrainerResponseDto trainer = this.trainersService.getById(id);
        Map<String, TrainerResponseDto> responseBody = new HashMap<>();
        responseBody.put("trainer", trainer);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{trainerId}/training_sessions", "/{trainerId}/training_sessions/" })
    public ResponseEntity<Map<String, List<TrainingSessionResponseDto>>> getTrainingSessions(@PathVariable Integer trainerId) {
        List<TrainingSessionResponseDto> trainingSessions = this.trainersService.getTrainerTrainingSessionsById(trainerId);
        Map<String, List<TrainingSessionResponseDto>> responseBody = new HashMap<>();
        responseBody.put("training_sessions", trainingSessions);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{trainerId}/training_sessions/{trainingSessionId}", "/{trainerId}/training_sessions/{trainingSessionId}/" })
    public ResponseEntity<Map<String, TrainingSessionResponseDto>> getTrainingSessions(@PathVariable Integer trainerId, @PathVariable Integer trainingSessionId) {
        TrainingSessionResponseDto trainingSession = this.trainersService.getTrainerTrainingSessionById(trainerId, trainingSessionId);
        Map<String, TrainingSessionResponseDto> responseBody = new HashMap<>();
        responseBody.put("training_session", trainingSession);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, TrainerResponseDto>> post(@Valid @RequestBody TrainerRequestDto request) {
        TrainerResponseDto createdTrainer = this.trainersService.create(request);
        Map<String, TrainerResponseDto> responseBody = new HashMap<>();
        responseBody.put("created_trainer", createdTrainer);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping({ "/{trainerId}/training_sessions", "/{trainerId}/training_sessions/" })
    public ResponseEntity<Map<String, List<TrainingSessionResponseDto>>> post(@PathVariable Integer trainerId, @RequestBody Map<String, List<Integer>> requestBody) {
        List<TrainingSessionResponseDto> assignedInTrainingSessions = this.trainersService.assignTrainerToTrainingSessions(trainerId, requestBody.get("training_sessions_ids"));
        Map<String, List<TrainingSessionResponseDto>> responseBody = new HashMap<>();
        responseBody.put("assigned_in_training_sessions", assignedInTrainingSessions);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "/{trainerId}/training_sessions/{trainingSessionId}", "/{trainerId}/training_sessions/{trainingSessionId}/" })
    public ResponseEntity<Map<String, TrainingSessionResponseDto>> post(@PathVariable Integer trainerId, @PathVariable Integer trainingSessionId) {
        TrainingSessionResponseDto assignedInTrainingSession = this.trainersService.assignTrainerToTrainingSession(trainerId, trainingSessionId);
        Map<String, TrainingSessionResponseDto> responseBody = new HashMap<>();
        responseBody.put("assigned_in_training_session", assignedInTrainingSession);
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, TrainerResponseDto>> put(@PathVariable Integer id, @RequestBody TrainerRequestDto updates) {
        TrainerResponseDto updatedTrainer = this.trainersService.updateById(id, updates);
        Map<String, TrainerResponseDto> responseBody = new HashMap<>();
        responseBody.put("updated_trainer", updatedTrainer);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<TrainerResponseDto>>> delete() {
        List<TrainerResponseDto> deletedTrainers = this.trainersService.deleteAll();
        Map<String, List<TrainerResponseDto>> responseBody = new HashMap<>();
        responseBody.put("deleted_trainers", deletedTrainers);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, TrainerResponseDto>> delete(@PathVariable Integer id) {
        TrainerResponseDto deletedTrainer = this.trainersService.deleteById(id);
        Map<String, TrainerResponseDto> responseBody = new HashMap<>();
        responseBody.put("deleted_trainer", deletedTrainer);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{trainerId}/training_sessions", "/{trainerId}/training_sessions/" })
    public ResponseEntity<Map<String, List<TrainingSessionResponseDto>>> cancelTrainerFromTrainingSessions(@PathVariable Integer trainerId) {
        List<TrainingSessionResponseDto> canceledFromTrainingSessions = this.trainersService.cancelTrainerFromTrainingSessions(trainerId);
        Map<String, List<TrainingSessionResponseDto>> responseBody = new HashMap<>();
        responseBody.put("canceled_from_training_sessions", canceledFromTrainingSessions);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{trainerId}/training_sessions/{trainingSessionId}", "/{trainerId}/training_sessions/{trainingSessionId}/" })
    public ResponseEntity<Map<String, TrainingSessionResponseDto>> cancelTrainerFromTrainingSession(@PathVariable Integer trainerId, @PathVariable Integer trainingSessionId) {
        TrainingSessionResponseDto canceledFromTrainingSession = this.trainersService.cancelTrainerFromTrainingSession(trainerId, trainingSessionId);
        Map<String, TrainingSessionResponseDto> responseBody = new HashMap<>();
        responseBody.put("canceled_from_training_session", canceledFromTrainingSession);
        return ResponseEntity.ok(responseBody);
    }
}
