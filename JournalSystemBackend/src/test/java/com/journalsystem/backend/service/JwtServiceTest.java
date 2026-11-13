package com.journalsystem.backend.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest{

    private JwtService jwtService;

    @BeforeEach
    void setUp(){
        jwtService = new JwtService();

        String secret = Base64.getEncoder().encodeToString(
                "01234567890123456789012345678901"
                        .getBytes(StandardCharsets.UTF_8)
        );

        ReflectionTestUtils.setField(jwtService, "secret", secret);
    }

    @Test
    void generateTokenShouldContainCorrectUserInformation(){

        String token = jwtService.generateToken(
                "doctor@test.com",
                "DOCTOR"
        );

        Claims claims = jwtService.extractClaims(token);

        assertEquals("doctor@test.com", claims.getSubject());
        assertEquals("DOCTOR", claims.get("role", String.class));
        assertNotNull(claims.getExpiration());
    }

    @Test
    void generatedTokenShouldBeValid(){

        String token = jwtService.generateToken(
                "patient@test.com",
                "PATIENT"
        );

        Assertions.assertDoesNotThrow(() -> jwtService.extractClaims(token));
    }
}

