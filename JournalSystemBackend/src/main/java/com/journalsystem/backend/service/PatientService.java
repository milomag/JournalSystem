package com.journalsystem.backend.service;

import com.journalsystem.backend.repository.IPatientRepository;
import com.journalsystem.backend.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService{

    private final IPatientRepository iPatientRepository;

    public PatientService(IPatientRepository iPatientRepository){
        this.iPatientRepository = iPatientRepository;
    }

    public Optional<Patient> getPatientById(Long id){
        return iPatientRepository.findById(id);
    }

    public Patient createPatient(Patient patient){
        return iPatientRepository.save(patient);
    }

    public Optional<Patient> getPatientByUserId(Long userId) {
        return iPatientRepository.findByUser_Id(userId);
    }
}