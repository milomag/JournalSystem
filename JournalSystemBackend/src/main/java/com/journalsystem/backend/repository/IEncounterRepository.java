package com.journalsystem.backend.repository;

import com.journalsystem.backend.entity.Encounter;
import com.journalsystem.backend.entity.Patient;
import com.journalsystem.backend.entity.compositIds.EncounterId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEncounterRepository extends JpaRepository<Encounter, Long> {
    boolean existsByEncounterId(EncounterId encounterId);
    List<Encounter> findByPatient(Patient patient);
}
