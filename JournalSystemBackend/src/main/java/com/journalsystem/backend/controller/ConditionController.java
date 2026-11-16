package com.journalsystem.backend.controller;

import com.journalsystem.backend.dto.request.ConditionRequest;
import com.journalsystem.backend.dto.response.ConditionResponse;
import com.journalsystem.backend.repository.IUserRepository;
import com.journalsystem.backend.service.ConditionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/condition")
public class ConditionController {

    private final ConditionService conditionService;
    private final IUserRepository userRepository;

    public ConditionController(ConditionService conditionService, IUserRepository userRepository) {
        this.conditionService = conditionService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAnyRole('DOCTOR', 'STAFF')")
    @PostMapping("/{patientId}")
    public ResponseEntity<ConditionResponse> addCondition(
            @PathVariable Long patientId,
            @Valid @RequestBody ConditionRequest request) {
        try {

            ConditionResponse response = conditionService.createCondition(request, patientId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('DOCTOR', 'STAFF')")
    @GetMapping("/{patientId}")
    public ResponseEntity<List<ConditionResponse>> getCondition(
            @PathVariable Long patientId) {

        List<ConditionResponse> conditions = conditionService.getConditions(patientId);

        if(conditions.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(conditions);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me")
    public ResponseEntity<List<ConditionResponse>> getMyCondition(
            Authentication authentication) {

        var user = userRepository.findByEmail(authentication.getName());
        if(user.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<ConditionResponse> conditions = conditionService.getConditions(user.get().getId());

        if(conditions.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(conditions);
    }
}
