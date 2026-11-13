package com.journalsystem.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    private String address;

    @ManyToOne
    @JoinColumn(name = "organisation_name")
    private Organisation organisation;

    public Location(){}

    public Location(String address) {
        this.address = address;
    }

    public String getAddress() {return address;}
}
