package com.journalsystem.backend.repository;

import com.journalsystem.backend.entity.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPractitionerRepository extends JpaRepository<Practitioner, Long> {
    boolean existsByEmail(String email);
    Optional<Practitioner> findByEmail(String email);
}
