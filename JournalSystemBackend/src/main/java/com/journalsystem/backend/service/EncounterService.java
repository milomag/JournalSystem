package com.journalsystem.backend.service;

import com.journalsystem.backend.dto.request.EncounterRequest;
import com.journalsystem.backend.dto.response.EncounterResponse;
import com.journalsystem.backend.entity.Encounter;
import com.journalsystem.backend.entity.Observation;
import com.journalsystem.backend.entity.Patient;
import com.journalsystem.backend.entity.Practitioner;
import com.journalsystem.backend.entity.compositIds.EncounterId;
import com.journalsystem.backend.repository.IEncounterRepository;
import com.journalsystem.backend.repository.IObservationRepository;
import com.journalsystem.backend.repository.IPractitionerRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;

@Service
public class EncounterService {

    private final IEncounterRepository encounterRepository;
    private final PatientService patientService;
    private final IPractitionerRepository practitionerRepository;
    private final IObservationRepository observationRepository;

    public EncounterService(IEncounterRepository encounterRepository, PatientService patientService, IPractitionerRepository practitionerRepository, IObservationRepository observationRepository) {
        this.encounterRepository = encounterRepository;
        this.patientService = patientService;
        this.practitionerRepository = practitionerRepository;
        this.observationRepository = observationRepository;
    }

    @Transactional
    public EncounterResponse createEncounter(EncounterRequest request, Long patientId) {
        Patient patient = patientService.getPatientById(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        Practitioner practitioner = practitionerRepository.findByEmail(request.practitionerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Practitioner not found"));

        System.out.println("test1 ");

        if (encounterRepository.existsByEncounterId(new EncounterId(request.time, patient, practitioner))) {
            throw new IllegalArgumentException("encounter already exists for patient");
        }
        System.out.println("test2");

        Encounter encounter = new Encounter(request.time, patient, practitioner, request.location);
        Encounter savedEncounter = encounterRepository.save(encounter);

        System.out.println("test3");

        List<Observation> observations = request.observations.stream().map(text ->new Observation(null, text, savedEncounter)).toList();
        List<Observation> savedObservations = observationRepository.saveAll(observations);

        System.out.println("test4");
        return new EncounterResponse(savedEncounter, savedObservations);
    }

    public List<EncounterResponse> getEncounters(Long patientId) {
        Patient patient = patientService.getPatientById(patientId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        List<Encounter> encounters = encounterRepository.findByPatient(patient);

        return encounters.stream().map(encounter -> {
            List<Observation> observations = observationRepository.findByEncounter(encounter);
            return new EncounterResponse(encounter, observations);
        }).toList();
    }
}
