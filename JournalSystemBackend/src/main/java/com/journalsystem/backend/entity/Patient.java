package com.journalsystem.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String phoneNumber;

    @Column(nullable = false)
    private String fullName;

    private LocalDate dateOfBirth;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Patient(){}

    public Patient(User user, String email, String phoneNumber, String fullName, LocalDate dateOfBirth){
        this.user = user;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getFullName(){
        return fullName;
    }

    public LocalDate getDateOfBirth(){
        return dateOfBirth;
    }

    public User getUser(){
        return user;
    }
}

