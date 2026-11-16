
package com.journalsystem.backend.controller;

import com.journalsystem.backend.dto.response.PatientNameResponse;
import com.journalsystem.backend.dto.response.PatientResponse;
import com.journalsystem.backend.repository.IUserRepository;
import com.journalsystem.backend.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController{

    private final PatientService patientService;
    private final IUserRepository userRepository;

    public PatientController(
            PatientService patientService,
            IUserRepository userRepository){
        this.patientService = patientService;
        this.userRepository = userRepository;
    }

    // Doctors can search for any patient
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long id){
        var patient = patientService.getPatientById(id);

        if(patient.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(patient.get().toPatientResponse());
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me")
    public ResponseEntity<PatientResponse> getMyPatientInformation( //Man bör väl kunna söka direct på patient?
            Authentication authentication){

        var user = userRepository.findByEmail(authentication.getName());

        if(user.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var patient = patientService.getPatientById(user.get().getId());

        if(patient.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(patient.get().toPatientResponse());
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/{id}")
    public ResponseEntity<PatientNameResponse> getPatientName(@PathVariable Long id){
        var patient = patientService.getPatientById(id);

        if(patient.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                new PatientNameResponse(patient.get().getId(), patient.get().getFullName())
        );
    }
}
