package com.journalsystem.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class EncounterRequest {

    @NotBlank
    public String practitionerEmail;

    @NotBlank
    public String time;

    public String location;

    public List<String> observations;

    public EncounterRequest(String practitionerEmail, String time, String location, List<String> observations) {
        this.practitionerEmail = practitionerEmail;
        this.time = time;
        this.location = location;
        this.observations = List.copyOf(observations);
    }

    public String getPractitionerEmail() {return practitionerEmail;}
    public String getTime() {return time;}
    public String getLocation() {return location;}
    public List<String> getObservations() {return List.copyOf(observations);}
}
