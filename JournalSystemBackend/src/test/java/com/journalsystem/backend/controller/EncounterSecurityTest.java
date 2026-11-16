
package com.journalsystem.backend.controller;

import com.journalsystem.backend.config.JwtAuthenticationFilter;
import com.journalsystem.backend.config.SecurityConfig;
import com.journalsystem.backend.repository.IUserRepository;
import com.journalsystem.backend.service.EncounterService;
import com.journalsystem.backend.service.JwtService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EncounterController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class EncounterSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EncounterService encounterService;

    @MockitoBean
    private IUserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void patientCannotGetOtherPatientsEncounters() throws Exception {
        mockMvc.perform(get("/api/encounter/1")
                        .with(user("patient").roles("PATIENT")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(encounterService);
    }

    @Test
    void doctorCanGetPatientsEncounters() throws Exception {
        when(encounterService.getEncounters(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/encounter/1")
                        .with(user("doctor").roles("DOCTOR")))
                .andExpect(status().isNotFound());

        verify(encounterService).getEncounters(1L);
    }

    @Test
    void staffCanGetPatientsEncounters() throws Exception {
        when(encounterService.getEncounters(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/encounter/1")
                        .with(user("staff").roles("STAFF")))
                .andExpect(status().isNotFound());

        verify(encounterService).getEncounters(1L);
    }

    @Test
    void patientCannotCreateEncounter() throws Exception {
        String requestBody = """
            {
                "practitionerEmail": "doctor@example.com",
                "time": "2026-11-16T10:00:00",
                "location": "Room 1",
                "observations": []
            }
            """;

        mockMvc.perform(post("/api/encounter/1")
                        .with(user("patient").roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());

        verifyNoInteractions(encounterService);
    }

    @Test
    void doctorCannotAccessOwnPatientEndpoint() throws Exception {
        mockMvc.perform(get("/api/encounter/me")
                        .with(user("doctor").roles("DOCTOR")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(encounterService);
    }

    @Test
    void staffCannotAccessOwnPatientEndpoint() throws Exception {
        mockMvc.perform(get("/api/encounter/me")
                        .with(user("staff").roles("STAFF")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(encounterService);
    }

    @Test
    void patientCanAccessOwnEncountersEndpoint() throws Exception {
        when(userRepository.findByEmail("patient"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/encounter/me")
                        .with(user("patient").roles("PATIENT")))
                .andExpect(status().isNotFound());

        verify(userRepository).findByEmail("patient");
    }
}
