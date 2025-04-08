package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.RoleRequestDto;
import org.green_building.excellent_training.dtos.RoleResponseDto;
import org.green_building.excellent_training.services.RolesService;
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
@RequestMapping("/roles")
// @CrossOrigin(origins = "http://localhost:6969")
public class RolesController {

    // @Autowired // this is field injection which is not recommended, use a constructor injection instead like down below
    private final RolesService rolesService;

    // note: only one Autowired constructor is allowed, if the class has only one constructor, the @Autowired is optional
    @Autowired
    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping({ "",  "/" }) // allow requests to both with and without trailing slash urls
    public ResponseEntity<Map<String, List<RoleResponseDto>>> get() {
        List<RoleResponseDto> roles = this.rolesService.getAll();
        Map<String, List<RoleResponseDto>> responseBody = new HashMap<>();
        responseBody.put("roles", roles);
        // return new ResponseEntity<>(responseBody, HttpStatus.OK);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, RoleResponseDto>> get(@PathVariable Integer id) {
        RoleResponseDto role = this.rolesService.getById(id);
        Map<String, RoleResponseDto> responseBody = new HashMap<>();
        responseBody.put("role", role);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, RoleResponseDto>> post(@Valid @RequestBody RoleRequestDto request) {
        RoleResponseDto response = this.rolesService.create(request);
        HttpStatus responseStatus = response == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        Map<String, RoleResponseDto> responseBody = new HashMap<>();
        responseBody.put("created_role", response);
        return ResponseEntity.status(responseStatus).body(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    // we will not Validate the dto coming here because not everything is required to be changed
    public ResponseEntity<Map<String, RoleResponseDto>> put(@PathVariable Integer id, @RequestBody RoleRequestDto updates) {
        RoleResponseDto updatedRole = this.rolesService.updateById(id, updates);
        Map<String, RoleResponseDto> responseBody = new HashMap<>();
        responseBody.put("updated_role", updatedRole);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<RoleResponseDto>>> delete() {
        List<RoleResponseDto> deletedRoles = this.rolesService.deleteAll();
        Map<String, List<RoleResponseDto>> responseBody = new HashMap<>();
        responseBody.put("deleted_roles", deletedRoles);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, RoleResponseDto>> delete(@PathVariable Integer id) {
        RoleResponseDto deletedRole = this.rolesService.deleteById(id);
        Map<String, RoleResponseDto> responseBody = new HashMap<>();
        responseBody.put("deleted_role", deletedRole);
        return ResponseEntity.ok(responseBody);
    }
}
