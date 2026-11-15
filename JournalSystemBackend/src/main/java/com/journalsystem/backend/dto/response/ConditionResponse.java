package com.journalsystem.backend.dto.response;

public class ConditionResponse {
    private String diagnosis;

    private String details;

    public ConditionResponse(String diagnosis, String details) {
        this.diagnosis = diagnosis;
        this.details = details;
    }

    public String getDiagnosis() {return diagnosis;};

    public String getDetails() {return details;}
}
