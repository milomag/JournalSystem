package com.journalsystem.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ConditionRequest {
    @NotBlank
    private String diagnosis;

    private String details;

    public ConditionRequest() {}

    public String getDiagnosis() {return diagnosis;};

    public String getDetails() {return details;}

    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public void setDetails(String details) { this.details = details;}
}
