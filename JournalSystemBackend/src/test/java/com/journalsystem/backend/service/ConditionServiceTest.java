
package com.journalsystem.backend.service;

import com.journalsystem.backend.dto.request.ConditionRequest;
import com.journalsystem.backend.entity.Condition;
import com.journalsystem.backend.entity.Patient;
import com.journalsystem.backend.entity.compositIds.ConditionId;
import com.journalsystem.backend.repository.IConditionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConditionServiceTest {

    @Mock
    private IConditionRepository conditionRepository;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private ConditionService conditionService;

    @Test
    void createConditionSuccessfully() {

        Patient patient = mock(Patient.class);

        ConditionRequest request = new ConditionRequest();
        request.setDiagnosis("Asthma");
        request.setDetails("Mild asthma");

        when(patientService.getPatientById(1L))
                .thenReturn(Optional.of(patient));

        when(conditionRepository.existsByConditionId(any(ConditionId.class)))
                .thenReturn(false);

        when(conditionRepository.save(any(Condition.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = conditionService.createCondition(request, 1L);

        assertNotNull(response);

        verify(conditionRepository).save(any(Condition.class));
    }

    @Test
    void createConditionFailsWhenPatientDoesNotExist() {
        ConditionRequest request = new ConditionRequest();
        request.setDiagnosis("Asthma");
        request.setDetails("Mild asthma");

        when(patientService.getPatientById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> conditionService.createCondition(request, 1L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verifyNoInteractions(conditionRepository);
    }

    @Test
    void createConditionFailsWhenDiagnosisAlreadyExists() {
        // Arrange
        Patient patient = mock(Patient.class);

        ConditionRequest request = new ConditionRequest();
        request.setDiagnosis("Asthma");
        request.setDetails("Mild asthma");

        when(patientService.getPatientById(1L))
                .thenReturn(Optional.of(patient));

        when(conditionRepository.existsByConditionId(any(ConditionId.class)))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> conditionService.createCondition(request, 1L)
        );

        verify(conditionRepository, never()).save(any(Condition.class));
    }

    @Test
    void getConditionsSuccessfully() {
        Patient patient = mock(Patient.class);

        Condition condition = new Condition(
                "Asthma",
                "Mild asthma",
                patient
        );

        when(patientService.getPatientById(1L))
                .thenReturn(Optional.of(patient));

        when(conditionRepository.findByPatient(patient))
                .thenReturn(List.of(condition));

        var response = conditionService.getConditions(1L);

        assertEquals(1, response.size());

        verify(conditionRepository).findByPatient(patient);
    }
}
