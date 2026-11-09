package com.journalsystem.backend.config;

import com.journalsystem.backend.repository.PatientRepository;
import com.journalsystem.backend.entity.Patient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner{

    private final PatientRepository patientRepository;

    public DataInitializer(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    @Override
    public void run(String... args){
        if(patientRepository.count() == 0){
            Patient patient = new Patient(
                    "test@example.com",
                    "0701234567",
                    "Test Patient",
                    java.time.LocalDate.of(2000, 5, 15)
            );

            patientRepository.save(patient);
        }
    }
}