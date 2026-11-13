package com.journalsystem.backend.entity.compositIds;

import com.journalsystem.backend.entity.Patient;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConditionId implements Serializable {
    @NotNull
    private String diagnosis;

    private Long patientId;

    public ConditionId() {}

    public ConditionId(String diagnosis, Patient patient) {
        this.diagnosis = diagnosis;
        this.patientId = patient.getId();
    }

    public String getDiagnosis() {return diagnosis;}

    public Long getPatientId() {return patientId;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConditionId that)) return false;
        return Objects.equals(diagnosis, that.getDiagnosis()) &&
                Objects.equals(patientId, that.getPatientId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(diagnosis, patientId);
    }
}
