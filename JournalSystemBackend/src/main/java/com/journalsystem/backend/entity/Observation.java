package com.journalsystem.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "observation")
public class Observation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "encounter_dateAndTime"),
            @JoinColumn(name = "encounter_patientId"),
            @JoinColumn(name = "encounter_practitionerId")})
    private Encounter encounter;

    public Observation() {}

    public Observation(Long id, String text, Encounter encounter) {
        this.id = id;
        this.text = text;
        this.encounter = encounter;
    }

    public Long getId() {return id;}

    public String getText() {return text;}

    public Encounter getEncounter() {return encounter;}
}
