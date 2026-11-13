package com.journalsystem.backend.entity;

import com.journalsystem.backend.dto.response.UserResponse;
import jakarta.persistence.*;

@Entity
@Table(name = "practitioner")
public class Practitioner extends User{

    public Practitioner() {
    }

    public Practitioner(Long id, String email, String password) {
        super(id, email, password);
    }

    public UserResponse toUserResponse() {
        return new UserResponse(id, email, Role.DOCTOR);
    }

    //Potentiellt lägga till en koppling till org
}
