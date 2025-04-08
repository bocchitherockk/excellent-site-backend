package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.ProfileRequestDto;
import org.green_building.excellent_training.dtos.ProfileResponseDto;
import org.green_building.excellent_training.services.ProfilesService;
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
@RequestMapping("/profiles")
// @CrossOrigin(origins = "http://localhost:6969")
public class ProfilesController {

    private final ProfilesService profilesService;

    @Autowired
    public ProfilesController(ProfilesService profilesService) {
        this.profilesService = profilesService;
    }

    @GetMapping({ "",  "/" })
    public ResponseEntity<Map<String, List<ProfileResponseDto>>> get() {
        List<ProfileResponseDto> profiles = this.profilesService.getAll();
        Map<String, List<ProfileResponseDto>> responseBody = new HashMap<>();
        responseBody.put("profiles", profiles);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, ProfileResponseDto>> get(@PathVariable Integer id) {
        ProfileResponseDto profile = this.profilesService.getById(id);
        Map<String, ProfileResponseDto> responseBody = new HashMap<>();
        responseBody.put("profile", profile);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, ProfileResponseDto>> post(@Valid @RequestBody ProfileRequestDto request) {
        ProfileResponseDto response = this.profilesService.create(request);
        HttpStatus responseStatus = response == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        Map<String, ProfileResponseDto> responseBody = new HashMap<>();
        responseBody.put("created_profile", response);
        return ResponseEntity.status(responseStatus).body(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    // we will not Validate the dto coming here because not everything is required to be changed
    public ResponseEntity<Map<String, ProfileResponseDto>> put(@PathVariable Integer id, @RequestBody ProfileRequestDto updates) {
        ProfileResponseDto updatedProfile = this.profilesService.updateById(id, updates);
        Map<String, ProfileResponseDto> responseBody = new HashMap<>();
        responseBody.put("updated_profile", updatedProfile);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<ProfileResponseDto>>> delete() {
        List<ProfileResponseDto> deletedProfiles = this.profilesService.deleteAll();
        Map<String, List<ProfileResponseDto>> responseBody = new HashMap<>();
        responseBody.put("deleted_profiles", deletedProfiles);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, ProfileResponseDto>> delete(@PathVariable Integer id) {
        ProfileResponseDto deletedProfile = this.profilesService.deleteById(id);
        Map<String, ProfileResponseDto> responseBody = new HashMap<>();
        responseBody.put("deleted_profile", deletedProfile);
        return ResponseEntity.ok(responseBody);
    }
}
