package com.journalsystem.backend.controller;

import com.journalsystem.backend.entity.Patient;
import com.journalsystem.backend.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController{

    private final PatientService patientService;

    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id){
        var patient = patientService.getPatientById(id);

        if(patient.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(patient.get());
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient){
        Patient createdPatient = patientService.createPatient(patient);
        return ResponseEntity.ok(createdPatient);
    }
}