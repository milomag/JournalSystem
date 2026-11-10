package com.journalsystem.backend.entity;

import jakarta.persistence.*;

@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user")
    private Patient patient;

    public User() {
    }

    public User(Long id, String email, String password, Role role, Patient patient) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Patient getPatient(){
        return patient;
    }
}