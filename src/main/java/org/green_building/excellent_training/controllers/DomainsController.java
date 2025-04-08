package org.green_building.excellent_training.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.green_building.excellent_training.dtos.DomainRequestDto;
import org.green_building.excellent_training.dtos.DomainResponseDto;
import org.green_building.excellent_training.services.DomainsService;
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
@RequestMapping("/domains")
// @CrossOrigin(origins = "http://localhost:6969")
public class DomainsController {

    private final DomainsService domainsService;

    @Autowired
    public DomainsController(DomainsService domainsService) {
        this.domainsService = domainsService;
    }

    @GetMapping({ "",  "/" })
    public ResponseEntity<Map<String, List<DomainResponseDto>>> get() {
        List<DomainResponseDto> domains = this.domainsService.getAll();
        Map<String, List<DomainResponseDto>> responseBody = new HashMap<>();
        responseBody.put("domains", domains);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, DomainResponseDto>> get(@PathVariable Integer id) {
        DomainResponseDto domain = this.domainsService.getById(id);
        Map<String, DomainResponseDto> responseBody = new HashMap<>();
        responseBody.put("domain", domain);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, DomainResponseDto>> post(@Valid @RequestBody DomainRequestDto request) {
        DomainResponseDto response = this.domainsService.create(request);
        HttpStatus responseStatus = response == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        Map<String, DomainResponseDto> responseBody = new HashMap<>();
        responseBody.put("created_domain", response);
        return ResponseEntity.status(responseStatus).body(responseBody);
    }

    @PutMapping({ "/{id}", "/{id}/" })
    // we will not Validate the dto coming here because not everything is required to be changed
    public ResponseEntity<Map<String, DomainResponseDto>> put(@PathVariable Integer id, @RequestBody DomainRequestDto updates) {
        DomainResponseDto updatedDomain = this.domainsService.updateById(id, updates);
        Map<String, DomainResponseDto> responseBody = new HashMap<>();
        responseBody.put("updated_domain", updatedDomain);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "", "/" })
    public ResponseEntity<Map<String, List<DomainResponseDto>>> delete() {
        List<DomainResponseDto> deletedDomains = this.domainsService.deleteAll();
        Map<String, List<DomainResponseDto>> responseBody = new HashMap<>();
        responseBody.put("deleted_domains", deletedDomains);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Map<String, DomainResponseDto>> delete(@PathVariable Integer id) {
        DomainResponseDto deletedDomain = this.domainsService.deleteById(id);
        Map<String, DomainResponseDto> responseBody = new HashMap<>();
        responseBody.put("deleted_domain", deletedDomain);
        return ResponseEntity.ok(responseBody);
    }
}
