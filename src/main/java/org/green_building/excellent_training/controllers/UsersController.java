package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.green_building.excellent_training.entities.User;
import org.green_building.excellent_training.services.UsersService;

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
    public ResponseEntity<Map<String, List<User>>> get() {
        Map<String, List<User>> responseBody = new HashMap<>();
        responseBody.put("users", this.usersService.getAll());
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, User>> get(@PathVariable Integer id) {
        Map<String, User> responseBody = new HashMap<>();
        responseBody.put("user", this.usersService.getById(id));
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, User>> post(@RequestBody User user) {
        System.out.println("got user from the request body: " + user);
        User insertedUser = this.usersService.create(user);
        Map<String, User> responseBody = new HashMap<>();
        responseBody.put("created_user", insertedUser);
        HttpStatus responseStatus = insertedUser == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        return ResponseEntity.status(responseStatus).body(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, User>> put(@PathVariable Integer id, @RequestBody User updates) {
        Map<String, User> responseBody = new HashMap<>();
        responseBody.put("updated_user", this.usersService.updateById(id, updates));
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<User>>> delete() {
        Map<String, List<User>> responseBody = new HashMap<>();
        responseBody.put("deleted_users", this.usersService.deleteAll());
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, User>> delete(@PathVariable Integer id) {
        Map<String, User> responseBody = new HashMap<>();
        responseBody.put("deleted_user", this.usersService.deleteById(id));
        return ResponseEntity.ok(responseBody);
    }
}
