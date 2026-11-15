package com.journalsystem.backend.controller;

import com.journalsystem.backend.dto.request.ConditionRequest;
import com.journalsystem.backend.dto.response.ConditionResponse;
import com.journalsystem.backend.service.ConditionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/condition")
public class ConditionController {

    private final ConditionService conditionService;

    public ConditionController(ConditionService conditionService) {
        this.conditionService = conditionService;
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/{userId}")
    public ResponseEntity<ConditionResponse> addCondition(
            @PathVariable Long userId,
            @Valid @RequestBody ConditionRequest request) {
        try {

            ConditionResponse response = conditionService.createCondition(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/{patientId}")
    public ResponseEntity<List<ConditionResponse>> getCondition(
            @PathVariable Long patientId) {

        List<ConditionResponse> conditions = conditionService.getConditions(patientId);

        if(conditions.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(conditions);
    }
}
