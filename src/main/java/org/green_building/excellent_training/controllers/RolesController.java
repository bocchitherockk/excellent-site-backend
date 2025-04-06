package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.RoleDto;
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
    public ResponseEntity<Map<String, List<RoleDto>>> get() {
        List<RoleDto> rolesDto = this.rolesService.getAll();
        Map<String, List<RoleDto>> responseBody = new HashMap<>();
        responseBody.put("roles", rolesDto);
        // return new ResponseEntity<>(responseBody, HttpStatus.OK);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, RoleDto>> get(@PathVariable Integer id) {
        RoleDto roleDto = this.rolesService.getById(id);
        Map<String, RoleDto> responseBody = new HashMap<>();
        responseBody.put("role", roleDto);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, RoleDto>> post(@Valid @RequestBody RoleDto roleDto) {
        RoleDto createdRoleDto = this.rolesService.create(roleDto);
        Map<String, RoleDto> responseBody = new HashMap<>();
        responseBody.put("created_role", createdRoleDto);
        HttpStatus responseStatus = createdRoleDto == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        return ResponseEntity.status(responseStatus).body(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    // we will not Validate the dto coming here because not everything is required to br changed
    public ResponseEntity<Map<String, RoleDto>> put(@PathVariable Integer id, @RequestBody RoleDto updatesDto) {
        RoleDto updatedRoleDto = this.rolesService.updateById(id, updatesDto);
        Map<String, RoleDto> responseBody = new HashMap<>();
        responseBody.put("updated_role", updatedRoleDto);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<RoleDto>>> delete() {
        List<RoleDto> deletedRolesDto = this.rolesService.deleteAll();
        Map<String, List<RoleDto>> responseBody = new HashMap<>();
        responseBody.put("deleted_roles", deletedRolesDto);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, RoleDto>> delete(@PathVariable Integer id) {
        RoleDto deletedRoleDto = this.rolesService.deleteById(id);
        Map<String, RoleDto> responseBody = new HashMap<>();
        responseBody.put("deleted_role", deletedRoleDto);
        return ResponseEntity.ok(responseBody);
    }
}
