package com.journalsystem.backend.entity;

import com.journalsystem.backend.entity.compositIds.ConditionId;
import jakarta.persistence.*;

@Entity
@Table(name = "patientCondition")
public class Condition {

    @EmbeddedId
    private ConditionId conditionId;

    private String details;

    @ManyToOne
    @MapsId("patientId")
    @JoinColumn(name = "patients_id")
    private Patient patient;

    public Condition() {}

    public Condition(String diagnosis, String details, Patient patient) {
        conditionId = new ConditionId(diagnosis, patient);
        this.details = details;
        this.patient = patient;
    }

    public ConditionId getConditionId() {return conditionId;}

    public String getDiagnosis() {return conditionId.getDiagnosis();}

    public String getDetails() {return details;}

    public Patient getPatient() {return patient;}
}
