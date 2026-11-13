
package com.journalsystem.backend.dto.request;

import com.journalsystem.backend.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class RegisterRequest{

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotNull
    private Role role;

    private String fullName;
    private LocalDate dateOfBirth;
    private String phoneNumber;

    public RegisterRequest(){}

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }

    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }

    public Role getRole(){ return role; }
    public void setRole(Role role){ this.role = role; }

    public String getFullName(){ return fullName; }
    public void setFullName(String fullName){ this.fullName = fullName; }

    public LocalDate getDateOfBirth(){ return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth){ this.dateOfBirth = dateOfBirth; }

    public String getPhoneNumber(){ return phoneNumber; }
    public void setPhoneNumber(String phoneNumber){ this.phoneNumber = phoneNumber; }
}
