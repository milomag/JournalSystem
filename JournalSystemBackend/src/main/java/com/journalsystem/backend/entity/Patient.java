package com.journalsystem.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.journalsystem.backend.dto.response.UserResponse;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient extends User{

    private String phoneNumber;

    @Column(nullable = false)
    private String fullName;

    private LocalDate dateOfBirth;

    public Patient(){}

    public Patient(Long id, String email, String password, String phoneNumber, String fullName, LocalDate dateOfBirth){
        super(id, email, password);
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
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

    public UserResponse toUserResponse() {
        return new UserResponse(id, email, Role.PATIENT);
    }
}

