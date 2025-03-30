package org.green_building.excellent_training.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.green_building.excellent_training.services.RolesService;
import org.green_building.excellent_training.entities.Role;

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
    public ResponseEntity<Map<String, List<Role>>> get() {
	Map<String, List<Role>> responseBody = new HashMap<>();
	responseBody.put("roles", this.rolesService.getAll());
	return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, Role>> get(@PathVariable Integer id) {
	Map<String, Role> responseBody = new HashMap<>();
	responseBody.put("role", this.rolesService.getById(id));
	return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, Role>> post(@RequestBody Role role) {
	Role insertedRole = this.rolesService.create(role);
	Map<String, Role> responseBody = new HashMap<>();
	responseBody.put("created_role", insertedRole);
	HttpStatus responseStatus = insertedRole == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
	return new ResponseEntity<>(responseBody, responseStatus);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, Role>> put(@PathVariable Integer id, @RequestBody Role modifications) {
	Map<String, Role> responseBody = new HashMap<>();
	responseBody.put("updated_role", this.rolesService.updateById(id, modifications));
	return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, Role>> delete(@PathVariable Integer id) {
	Map<String, Role> responseBody = new HashMap<>();
	responseBody.put("deleted_role", this.rolesService.deleteById(id));
	return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}

