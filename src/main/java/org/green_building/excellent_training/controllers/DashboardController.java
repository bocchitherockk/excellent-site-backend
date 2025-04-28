package org.green_building.excellent_training.controllers;

import org.green_building.excellent_training.services.TrainingSessionsService;
import org.green_building.excellent_training.services.EmployersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final TrainingSessionsService trainingSessionsService;
    private final EmployersService employersService;

    @Autowired
    public DashboardController(TrainingSessionsService trainingSessionsService, EmployersService employersService) {
        this.trainingSessionsService = trainingSessionsService;
        this.employersService = employersService;
    }

    @GetMapping({ "/training_sessions", "/training_sessions/" })
    public ResponseEntity<Map<String, List<Double>>> getTrainingSessionsByDomain() {
        Map<String, List<Double>> sessionsByDomain = trainingSessionsService.getSessionsBudgetSumByDomainForPastFiveYears();
        return ResponseEntity.ok(sessionsByDomain);
    }

    @GetMapping({ "/employers", "/employers/" })
    public ResponseEntity<Map<String, List<?>>> getEmployersWithTrainersCount() {
        Map<String, List<?>> employersData = employersService.getEmployersWithTrainersCount();
        return ResponseEntity.ok(employersData);
    }
}
