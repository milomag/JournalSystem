package com.journalsystem.backend.dto.response;

import com.journalsystem.backend.entity.Encounter;
import com.journalsystem.backend.entity.Observation;

import java.util.List;

public class EncounterResponse {

    private String practitionerEmail;

    private String time;

    private String location;

    private List<String> observations;

    public EncounterResponse(Encounter encounter, List<Observation> observations) {
        this.practitionerEmail = encounter.getPractitioner().getEmail();
        this.time = encounter.getDateAndTime();
        this.location = encounter.getLocation();
        this.observations = observations.stream().map(Observation::getText).toList();
    }

    public String getPractitionerEmail() {return practitionerEmail;}
    public String getTime() {return time;}
    public String getLocation() {return location;}
    public List<String> getObservations() {return List.copyOf(observations);}
}
