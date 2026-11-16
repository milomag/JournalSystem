package com.journalsystem.backend.repository;

import com.journalsystem.backend.entity.Encounter;
import com.journalsystem.backend.entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IObservationRepository extends JpaRepository<Observation, Long> {
    List<Observation> findByEncounter(Encounter encounter);
}
