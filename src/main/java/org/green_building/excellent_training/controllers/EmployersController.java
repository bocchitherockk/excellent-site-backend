package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.EmployerRequestDto;
import org.green_building.excellent_training.dtos.EmployerResponseDto;
import org.green_building.excellent_training.services.EmployersService;
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
@RequestMapping("/employers")
// @CrossOrigin(origins = "http://localhost:6969")
public class EmployersController {

    private final EmployersService employersService;

    @Autowired
    public EmployersController(EmployersService employersService) {
        this.employersService = employersService;
    }

    @GetMapping({ "",  "/" })
    public ResponseEntity<Map<String, List<EmployerResponseDto>>> get() {
        List<EmployerResponseDto> employers = this.employersService.getAll();
        Map<String, List<EmployerResponseDto>> responseBody = new HashMap<>();
        responseBody.put("employers", employers);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, EmployerResponseDto>> get(@PathVariable Integer id) {
        EmployerResponseDto employer = this.employersService.getById(id);
        Map<String, EmployerResponseDto> responseBody = new HashMap<>();
        responseBody.put("employer", employer);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, EmployerResponseDto>> post(@Valid @RequestBody EmployerRequestDto request) {
        EmployerResponseDto response = this.employersService.create(request);
        HttpStatus responseStatus = response == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        Map<String, EmployerResponseDto> responseBody = new HashMap<>();
        responseBody.put("created_employer", response);
        return ResponseEntity.status(responseStatus).body(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    // we will not Validate the dto coming here because not everything is required to be changed
    public ResponseEntity<Map<String, EmployerResponseDto>> put(@PathVariable Integer id, @RequestBody EmployerRequestDto updates) {
        EmployerResponseDto updatedEmployer = this.employersService.updateById(id, updates);
        Map<String, EmployerResponseDto> responseBody = new HashMap<>();
        responseBody.put("updated_employer", updatedEmployer);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<EmployerResponseDto>>> delete() {
        List<EmployerResponseDto> deletedEmployers = this.employersService.deleteAll();
        Map<String, List<EmployerResponseDto>> responseBody = new HashMap<>();
        responseBody.put("deleted_employers", deletedEmployers);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, EmployerResponseDto>> delete(@PathVariable Integer id) {
        EmployerResponseDto deletedEmployer = this.employersService.deleteById(id);
        Map<String, EmployerResponseDto> responseBody = new HashMap<>();
        responseBody.put("deleted_employer", deletedEmployer);
        return ResponseEntity.ok(responseBody);
    }
}
