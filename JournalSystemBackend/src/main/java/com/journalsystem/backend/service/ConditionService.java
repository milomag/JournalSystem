package com.journalsystem.backend.service;

import com.journalsystem.backend.dto.request.ConditionRequest;
import com.journalsystem.backend.dto.response.ConditionResponse;
import com.journalsystem.backend.entity.Condition;
import com.journalsystem.backend.entity.Patient;
import com.journalsystem.backend.entity.compositIds.ConditionId;
import com.journalsystem.backend.repository.IConditionRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ConditionService {
    private final IConditionRepository conditionRepository;
    private final PatientService patientService;

    public ConditionService(IConditionRepository conditionRepository, PatientService patientService) {
        this.conditionRepository = conditionRepository;
        this.patientService = patientService;
    }

    @Transactional
    public ConditionResponse createCondition(ConditionRequest request, Long patientId) throws IllegalArgumentException {

        Patient patient = patientService.getPatientById(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        if(conditionRepository.existsByConditionId(new ConditionId(request.getDiagnosis(), patient))) {
            throw new IllegalArgumentException("diagnosis already exists for patient");
        }

        Condition condition = new Condition(request.getDiagnosis(), request.getDetails(), patient);

        Condition savedCondition = conditionRepository.save(condition);

        return savedCondition.toConditionResponse();
    }


    public List<ConditionResponse> getConditions(Long patientId) {
        Patient patient = patientService.getPatientById(patientId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        return conditionRepository.findByPatient(patient).stream()
                .map(Condition::toConditionResponse).toList();
    }
}
