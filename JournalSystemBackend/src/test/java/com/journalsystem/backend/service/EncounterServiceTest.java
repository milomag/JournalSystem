
package com.journalsystem.backend.service;

import com.journalsystem.backend.dto.request.EncounterRequest;
import com.journalsystem.backend.entity.Encounter;
import com.journalsystem.backend.entity.Patient;
import com.journalsystem.backend.entity.Practitioner;
import com.journalsystem.backend.entity.Observation;
import com.journalsystem.backend.entity.compositIds.EncounterId;
import com.journalsystem.backend.repository.IEncounterRepository;
import com.journalsystem.backend.repository.IObservationRepository;
import com.journalsystem.backend.repository.IPractitionerRepository;
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
class EncounterServiceTest {

    @Mock
    private IEncounterRepository encounterRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private IPractitionerRepository practitionerRepository;

    @Mock
    private IObservationRepository observationRepository;

    @InjectMocks
    private EncounterService encounterService;

    @Test
    void createEncounterSuccessfully() {
        // Arrange
        Patient patient = mock(Patient.class);
        Practitioner practitioner = mock(Practitioner.class);

        EncounterRequest request = new EncounterRequest(
                "doctor@test.com",
                "2026-11-16 10:00:00",
                "Hospital",
                List.of("Blood pressure checked", "Temperature measured")
        );

        when(patientService.getPatientById(1L))
                .thenReturn(Optional.of(patient));

        when(practitionerRepository.findByEmail("doctor@test.com"))
                .thenReturn(Optional.of(practitioner));

        when(encounterRepository.existsByEncounterId(any(EncounterId.class)))
                .thenReturn(false);

        when(encounterRepository.save(any(Encounter.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(observationRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var response = encounterService.createEncounter(request, 1L);

        // Assert
        assertNotNull(response);

        verify(encounterRepository).save(any(Encounter.class));
        verify(observationRepository).saveAll(anyList());
    }

    @Test
    void createEncounterFailsWhenPatientDoesNotExist() {
        // Arrange
        EncounterRequest request = new EncounterRequest(
                "doctor@test.com",
                "2026-11-16 10:00:00",
                "Hospital",
                List.of()
        );

        when(patientService.getPatientById(1L))
                .thenReturn(Optional.empty());

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> encounterService.createEncounter(request, 1L)
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verifyNoInteractions(practitionerRepository);
        verifyNoInteractions(encounterRepository);
    }

    @Test
    void createEncounterFailsWhenPractitionerDoesNotExist() {
        // Arrange
        Patient patient = mock(Patient.class);

        EncounterRequest request = new EncounterRequest(
                "unknown@test.com",
                "2026-11-16 10:00:00",
                "Hospital",
                List.of()
        );

        when(patientService.getPatientById(1L))
                .thenReturn(Optional.of(patient));

        when(practitionerRepository.findByEmail("unknown@test.com"))
                .thenReturn(Optional.empty());

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> encounterService.createEncounter(request, 1L)
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verifyNoInteractions(encounterRepository);
        verifyNoInteractions(observationRepository);
    }

    @Test
    void createEncounterFailsWhenEncounterAlreadyExists() {
        // Arrange
        Patient patient = mock(Patient.class);
        Practitioner practitioner = mock(Practitioner.class);

        EncounterRequest request = new EncounterRequest(
                "doctor@test.com",
                "2026-11-16 10:00:00",
                "Hospital",
                List.of()
        );

        when(patientService.getPatientById(1L))
                .thenReturn(Optional.of(patient));

        when(practitionerRepository.findByEmail("doctor@test.com"))
                .thenReturn(Optional.of(practitioner));

        when(encounterRepository.existsByEncounterId(any(EncounterId.class)))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> encounterService.createEncounter(request, 1L)
        );

        verify(encounterRepository, never()).save(any(Encounter.class));
        verifyNoInteractions(observationRepository);
    }
}
