package com.journalsystem.backend.dto.response;

public class PatientNameResponse{

    private Long id;

    private String fullName;

    public PatientNameResponse(Long id, String fullName){
        this.fullName = fullName;
        this.id = id;
    }

    public String getFullName(){
        return fullName;
    }
    public Long getId() {return id;}
}