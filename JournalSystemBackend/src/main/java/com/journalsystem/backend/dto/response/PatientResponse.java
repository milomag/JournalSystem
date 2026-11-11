package com.journalsystem.backend.dto.response;

import java.time.LocalDate;

public class PatientResponse{

    private final Long id;
    private final String email;
    private final String fullName;
    private final LocalDate dateOfBirth;
    private final String phoneNumber;

    public PatientResponse(Long id, String email, String fullName,
                           LocalDate dateOfBirth, String phoneNumber){
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

    public Long getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getFullName(){
        return fullName;
    }

    public LocalDate getDateOfBirth(){
        return dateOfBirth;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
}