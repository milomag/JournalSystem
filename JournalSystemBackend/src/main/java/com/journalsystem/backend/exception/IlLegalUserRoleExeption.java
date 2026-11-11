package com.journalsystem.backend.exception;

import com.journalsystem.backend.entity.Role;

public class IlLegalUserRoleExeption extends RuntimeException {
    public IlLegalUserRoleExeption(Role role) {
        super("Invalid user role submitted: " + role);
    }
}
