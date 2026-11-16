package com.journalsystem.backend.entity.compositIds;

import com.journalsystem.backend.entity.Patient;
import com.journalsystem.backend.entity.Practitioner;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Embeddable
public class EncounterId implements Serializable {
    @NotNull
    private String dateAndTime;

    private Long patientId;

    private Long practitionerId;

    public EncounterId() {}

    public EncounterId(String dateAndTime, Patient patient, Practitioner practitioner) {
        this.dateAndTime = dateAndTime;
        patientId = patient.getId();
        practitionerId = practitioner.getId();
    }

    public String getDateAndTime() {return dateAndTime;}

    public Long getPatientId() {return patientId;}

    private Long getPractitionerId() {return practitionerId;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EncounterId that)) return false;
        return dateAndTime.equals(that.getDateAndTime()) &&
                Objects.equals(patientId, that.getPatientId()) &&
                Objects.equals(practitionerId, that.getPractitionerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateAndTime, patientId, practitionerId);
    }
}
