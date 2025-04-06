package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.UserDto;
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
    public ResponseEntity<Map<String, List<UserDto>>> get() {
        List<UserDto> usersDto = this.usersService.getAll();
        Map<String, List<UserDto>> responseBody = new HashMap<>();
        responseBody.put("users", usersDto);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, UserDto>> get(@PathVariable Integer id) {
        UserDto userDto = this.usersService.getById(id);
        Map<String, UserDto> responseBody = new HashMap<>();
        responseBody.put("user", userDto);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, UserDto>> post(@Valid @RequestBody UserDto userDto) {
        UserDto createdUserDto = this.usersService.create(userDto);
        Map<String, UserDto> responseBody = new HashMap<>();
        responseBody.put("created_user", createdUserDto);
        HttpStatus responseStatus = createdUserDto == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        return ResponseEntity.status(responseStatus).body(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, UserDto>> put(@PathVariable Integer id, @RequestBody UserDto updatesDto) {
        UserDto updatedUserDto = this.usersService.updateById(id, updatesDto);
        Map<String, UserDto> responseBody = new HashMap<>();
        responseBody.put("updated_user", updatedUserDto);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<UserDto>>> delete() {
        List<UserDto> deletedUsersDto = this.usersService.deleteAll();
        Map<String, List<UserDto>> responseBody = new HashMap<>();
        responseBody.put("deleted_users", deletedUsersDto);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, UserDto>> delete(@PathVariable Integer id) {
        UserDto deletedUserDto = this.usersService.deleteById(id);
        Map<String, UserDto> responseBody = new HashMap<>();
        responseBody.put("deleted_user", deletedUserDto);
        return ResponseEntity.ok(responseBody);
    }
}
