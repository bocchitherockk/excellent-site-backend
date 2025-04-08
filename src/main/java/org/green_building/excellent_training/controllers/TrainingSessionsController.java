package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, TrainingSessionResponseDto>> post(@Valid @RequestBody TrainingSessionRequestDto request) {
        TrainingSessionResponseDto createdTrainingSession = this.trainingSessionsService.create(request);
        HttpStatus responseStatus = createdTrainingSession == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        Map<String, TrainingSessionResponseDto> responseBody = new HashMap<>();
        responseBody.put("created_training_session", createdTrainingSession);
        return ResponseEntity.status(responseStatus).body(responseBody);
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
}
