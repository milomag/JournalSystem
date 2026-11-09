package com.journalsystem.backend.config;

import com.journalsystem.backend.repository.IPatientRepository;
import com.journalsystem.backend.entity.Patient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner{

    private final IPatientRepository iPatientRepository;

    public DataInitializer(IPatientRepository iPatientRepository){
        this.iPatientRepository = iPatientRepository;
    }

    @Override
    public void run(String... args){
        if(iPatientRepository.count() == 0){
            Patient patient = new Patient(
                    "test@example.com",
                    "0701234567",
                    "Test Patient",
                    java.time.LocalDate.of(2000, 5, 15)
            );

            iPatientRepository.save(patient);
        }
    }
}