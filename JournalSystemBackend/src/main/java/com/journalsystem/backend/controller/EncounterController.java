package com.journalsystem.backend.controller;

import com.journalsystem.backend.dto.request.EncounterRequest;
import com.journalsystem.backend.dto.response.EncounterResponse;
import com.journalsystem.backend.repository.IUserRepository;
import com.journalsystem.backend.service.EncounterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encounter")
public class EncounterController {
    private final EncounterService encounterService;
    private final IUserRepository userRepository;

    public EncounterController(EncounterService encounterService, IUserRepository userRepository) {
        this.encounterService = encounterService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/{patientId}")
    public ResponseEntity<EncounterResponse> addEncounter(
            @PathVariable Long patientId,
            @Valid @RequestBody EncounterRequest request) {

        try {
            EncounterResponse response = encounterService.createEncounter(request, patientId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/{patientId}")
    public ResponseEntity<List<EncounterResponse>> getEncounter(
            @PathVariable Long patientId) {

        List<EncounterResponse> encounters = encounterService.getEncounters(patientId);

        if(encounters.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(encounters);
    }

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/staff/{patientId}")
    public ResponseEntity<EncounterResponse> addEncounterStaff(
            @PathVariable Long patientId,
            @Valid @RequestBody EncounterRequest request) {

        try {
            EncounterResponse response = encounterService.createEncounter(request, patientId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/{patientId}")
    public ResponseEntity<List<EncounterResponse>> getEncounterStaff(
            @PathVariable Long patientId) {

        List<EncounterResponse> encounters = encounterService.getEncounters(patientId);

        if(encounters.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(encounters);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me")
    public ResponseEntity<List<EncounterResponse>> getMyEncounters(
            Authentication authentication) {

        var user = userRepository.findByEmail(authentication.getName());
        if(user.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<EncounterResponse> encounters = encounterService.getEncounters(user.get().getId());

        if(encounters.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(encounters);
    }
}
