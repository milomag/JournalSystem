package com.journalsystem.backend.service;

import com.journalsystem.backend.entity.Practitioner;
import com.journalsystem.backend.repository.IPractitionerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PractitionerService {
    private final IPractitionerRepository practitionerRepository;

    public PractitionerService(IPractitionerRepository practitionerRepository) {
        this.practitionerRepository = practitionerRepository;
    }

    public Optional<Practitioner> getPractitionerByEmail(String email) {return practitionerRepository.findByEmail(email);}
}
