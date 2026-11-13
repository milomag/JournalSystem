
package com.journalsystem.backend.service;

import com.journalsystem.backend.entity.Patient;
import com.journalsystem.backend.entity.Practitioner;
import com.journalsystem.backend.repository.IMessageRepository;
import com.journalsystem.backend.repository.IUserRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest{

    @Mock
    private IMessageRepository messageRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    void patientShouldBeAbleToContactDoctor(){

        Patient patient = new Patient();
        Practitioner doctor = new Practitioner();

        when(userRepository.findByEmail("patient@test.com"))
                .thenReturn(Optional.of(patient));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(doctor));

        when(messageRepository.findConversation(any(), any()))
                .thenReturn(List.of());

        assertDoesNotThrow(() ->
                messageService.getConversation("patient@test.com", 2L)
        );

        verify(messageRepository).findConversation(any(), any());
    }

    @Test
    void patientShouldNotBeAbleToContactAnotherPatient(){

        Patient patient = new Patient();
        Patient otherPatient = new Patient();

        when(userRepository.findByEmail("patient@test.com"))
                .thenReturn(Optional.of(patient));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(otherPatient));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> messageService.getConversation("patient@test.com", 2L)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        verifyNoInteractions(messageRepository);
    }

    @Test
    void doctorShouldNotBeAbleToContactStaff(){

        Practitioner doctor = new Practitioner();
        com.journalsystem.backend.entity.Staff staff =
                new com.journalsystem.backend.entity.Staff();

        when(userRepository.findByEmail("doctor@test.com"))
                .thenReturn(Optional.of(doctor));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(staff));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> messageService.getConversation("doctor@test.com", 2L)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        verifyNoInteractions(messageRepository);
    }
}
