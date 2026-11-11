package com.journalsystem.backend.dto.response;

public class PatientNameResponse{

    private String fullName;

    public PatientNameResponse(String fullName){
        this.fullName = fullName;
    }

    public String getFullName(){
        return fullName;
    }
}