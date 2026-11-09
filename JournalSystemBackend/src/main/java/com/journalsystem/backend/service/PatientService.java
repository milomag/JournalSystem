package com.journalsystem.backend.service;

import com.journalsystem.backend.repository.PatientRepository;
import com.journalsystem.backend.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService{

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    public Optional<Patient> getPatientById(Long id){
        return patientRepository.findById(id);
    }
}