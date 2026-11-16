package com.journalsystem.backend.entity;

import com.journalsystem.backend.entity.compositIds.EncounterId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

@Entity
@Table(name = "encounter")
public class Encounter {

    @EmbeddedId
    private EncounterId encounterId;

    @ManyToOne
    @MapsId("patientId")
    @JoinColumn(name = "patients_id")
    private Patient patient;

    @ManyToOne
    @MapsId("practitionerId")
    @JoinColumn(name = "practitioner_id")
    private Practitioner practitioner;

    private String location;

    public Encounter() {}

    public Encounter(String dateAndTime, Patient patient, Practitioner practitioner, String location) {
        encounterId = new EncounterId(dateAndTime, patient, practitioner);
        this.patient = patient;
        this.practitioner = practitioner;
        this.location = location;
    }

    public EncounterId getEncounterId() {return encounterId;}

    public String getDateAndTime() {return encounterId.getDateAndTime();}

    public Patient getPatient() {return patient;}

    public Practitioner getPractitioner() {return practitioner;}

    public String getLocation() {return location;}
}
