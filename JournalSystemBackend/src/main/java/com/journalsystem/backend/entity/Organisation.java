package com.journalsystem.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "organisation")
public class Organisation {
    @Id
    private String name;

    private String description;

    public Organisation() {}

    public Organisation(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {return name;}

    public String getDescription() {return description;}
}
