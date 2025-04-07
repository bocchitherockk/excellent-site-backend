package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.StructureRequestDto;
import org.green_building.excellent_training.dtos.StructureResponseDto;
import org.green_building.excellent_training.services.StructuresService;
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
@RequestMapping("/structures")
// @CrossOrigin(origins = "http://localhost:6969")
public class StructuresController {

    // @Autowired // this is field injection which is not recommended, use a constructor injection instead like down below
    private final StructuresService structuresService;

    // note: only one Autowired constructor is allowed, if the class has only one constructor, the @Autowired is optional
    @Autowired
    public StructuresController(StructuresService structuresService) {
        this.structuresService = structuresService;
    }

    @GetMapping({ "",  "/" }) // allow requests to both with and without trailing slash urls
    public ResponseEntity<Map<String, List<StructureResponseDto>>> get() {
        List<StructureResponseDto> structures = this.structuresService.getAll();
        Map<String, List<StructureResponseDto>> responseBody = new HashMap<>();
        responseBody.put("structures", structures);
        // return new ResponseEntity<>(responseBody, HttpStatus.OK);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, StructureResponseDto>> get(@PathVariable Integer id) {
        StructureResponseDto structure = this.structuresService.getById(id);
        Map<String, StructureResponseDto> responseBody = new HashMap<>();
        responseBody.put("structure", structure);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, StructureResponseDto>> post(@Valid @RequestBody StructureRequestDto request) {
        StructureResponseDto response = this.structuresService.create(request);
        HttpStatus responseStatus = response == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        Map<String, StructureResponseDto> responseBody = new HashMap<>();
        responseBody.put("created_structure", response);
        return ResponseEntity.status(responseStatus).body(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    // we will not Validate the dto coming here because not everything is required to br changed
    public ResponseEntity<Map<String, StructureResponseDto>> put(@PathVariable Integer id, @RequestBody StructureRequestDto updates) {
        StructureResponseDto updatedStructure = this.structuresService.updateById(id, updates);
        Map<String, StructureResponseDto> responseBody = new HashMap<>();
        responseBody.put("updated_structure", updatedStructure);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<StructureResponseDto>>> delete() {
        List<StructureResponseDto> deletedStructures = this.structuresService.deleteAll();
        Map<String, List<StructureResponseDto>> responseBody = new HashMap<>();
        responseBody.put("deleted_structures", deletedStructures);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, StructureResponseDto>> delete(@PathVariable Integer id) {
        StructureResponseDto deletedStructure = this.structuresService.deleteById(id);
        Map<String, StructureResponseDto> responseBody = new HashMap<>();
        responseBody.put("deleted_structure", deletedStructure);
        return ResponseEntity.ok(responseBody);
    }
}
