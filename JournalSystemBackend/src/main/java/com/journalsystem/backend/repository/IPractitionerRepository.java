package com.journalsystem.backend.repository;

import com.journalsystem.backend.entity.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPractitionerRepository extends JpaRepository<Practitioner, Long> {
    boolean existsByEmail(String email);
}
