package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.UserRequestDto;
import org.green_building.excellent_training.dtos.UserResponseDto;
import org.green_building.excellent_training.services.UsersService;
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
@RequestMapping("/users")
// @CrossOrigin(origins = "http://localhost:6969")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping({ "",  "/" })
    public ResponseEntity<Map<String, List<UserResponseDto>>> get() {
        List<UserResponseDto> users = this.usersService.getAll();
        Map<String, List<UserResponseDto>> responseBody = new HashMap<>();
        responseBody.put("users", users);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, UserResponseDto>> get(@PathVariable Integer id) {
        UserResponseDto user = this.usersService.getById(id);
        Map<String, UserResponseDto> responseBody = new HashMap<>();
        responseBody.put("user", user);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, UserResponseDto>> post(@Valid @RequestBody UserRequestDto request) {
        UserResponseDto createdUser = this.usersService.create(request);
        HttpStatus responseStatus = createdUser == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        Map<String, UserResponseDto> responseBody = new HashMap<>();
        responseBody.put("created_user", createdUser);
        return ResponseEntity.status(responseStatus).body(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, UserResponseDto>> put(@PathVariable Integer id, @RequestBody UserRequestDto updates) {
        UserResponseDto updatedUser = this.usersService.updateById(id, updates);
        Map<String, UserResponseDto> responseBody = new HashMap<>();
        responseBody.put("updated_user", updatedUser);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<UserResponseDto>>> delete() {
        List<UserResponseDto> deletedUsers = this.usersService.deleteAll();
        Map<String, List<UserResponseDto>> responseBody = new HashMap<>();
        responseBody.put("deleted_users", deletedUsers);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, UserResponseDto>> delete(@PathVariable Integer id) {
        UserResponseDto deletedUser = this.usersService.deleteById(id);
        Map<String, UserResponseDto> responseBody = new HashMap<>();
        responseBody.put("deleted_user", deletedUser);
        return ResponseEntity.ok(responseBody);
    }
}
