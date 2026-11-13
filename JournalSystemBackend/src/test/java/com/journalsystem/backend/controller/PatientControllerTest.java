
package com.journalsystem.backend.controller;

import com.journalsystem.backend.dto.response.PatientResponse;
import com.journalsystem.backend.entity.Patient;
import com.journalsystem.backend.repository.IUserRepository;
import com.journalsystem.backend.service.PatientService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private PatientController patientController;

    @Test
    void getPatient_shouldReturnPatient_whenPatientExists() {
        // Arrange
        Long patientId = 1L;

        Patient patient = mock(Patient.class);
        PatientResponse response = mock(PatientResponse.class);

        when(patientService.getPatientById(patientId))
                .thenReturn(Optional.of(patient));

        when(patient.toPatientResponse()).thenReturn(response);

        // Act
        var result = patientController.getPatient(patientId);

        // Assert
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());

        verify(patientService).getPatientById(patientId);
    }

    @Test
    void getPatient_shouldReturnNotFound_whenPatientDoesNotExist() {
        // Arrange
        Long patientId = 1L;

        when(patientService.getPatientById(patientId))
                .thenReturn(Optional.empty());

        // Act
        var result = patientController.getPatient(patientId);

        // Assert
        assertEquals(404, result.getStatusCode().value());
        assertNull(result.getBody());

        verify(patientService).getPatientById(patientId);
    }
}
