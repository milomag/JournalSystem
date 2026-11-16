package com.journalsystem.backend.repository;

import com.journalsystem.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPatientRepository extends JpaRepository<Patient, Long>{

    boolean existsByEmail(String email);
    Optional<Patient> findByEmail(String email);
}
