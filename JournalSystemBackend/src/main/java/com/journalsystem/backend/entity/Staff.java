package com.journalsystem.backend.entity;

import com.journalsystem.backend.dto.response.UserResponse;
import jakarta.persistence.Entity;

@Entity
public class Staff extends User{

    public Staff() {}

    public Staff(Long id, String email, String password) {
        super(id, email, password);
    }

    public UserResponse toUserResponse() {
        return new UserResponse(id, email, Role.STAFF);
    }
}
