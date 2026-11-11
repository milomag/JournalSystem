package com.journalsystem.backend.service;

import com.journalsystem.backend.dto.response.AuthResponse;
import com.journalsystem.backend.entity.*;
import com.journalsystem.backend.exception.EmailAlreadyRegisteredException;
import com.journalsystem.backend.exception.IlLegalUserRoleExeption;
import com.journalsystem.backend.exception.InvalidCredentialsException;
import com.journalsystem.backend.repository.IPractitionerRepository;
import com.journalsystem.backend.repository.IStaffRepository;
import com.journalsystem.backend.dto.request.LoginRequest;
import com.journalsystem.backend.dto.request.RegisterRequest;
import com.journalsystem.backend.dto.response.UserResponse;
import com.journalsystem.backend.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService{
    private final IUserRepository iUserRepository;
    private final IPractitionerRepository iPractitionerRepository;
    private final IStaffRepository iStaffRepository;
    private final PatientService patientService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(IUserRepository iUserRepository,
                       IPractitionerRepository iPractitionerRepository,
                       IStaffRepository iStaffRepository,
                       PatientService patientService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService){
        this.iUserRepository = iUserRepository;
        this.iPractitionerRepository = iPractitionerRepository;
        this.iStaffRepository = iStaffRepository;
        this.patientService = patientService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserResponse registerUser(RegisterRequest request){
        if(iUserRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyRegisteredException();
        }
        if((request.getRole() == Role.DOCTOR ||
                request.getRole() == Role.STAFF)
                && !isEmailVerified(request.getEmail())){

            throw new IllegalArgumentException("Email verification failed.");
        }
        switch (request.getRole()) {
            case PATIENT -> {
                if(request.getFullName() == null || request.getFullName().isBlank()
                        || request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()
                        || request.getDateOfBirth() == null){
                    throw new IllegalArgumentException("All patient information is required.");
                }
                Patient patient = new Patient(
                        null,
                        request.getEmail(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getPhoneNumber(),
                        request.getFullName(),
                        request.getDateOfBirth()
                );
                patientService.createPatient(patient);
                return patient.toUserResponse();
            }

            case DOCTOR -> {
                Practitioner practitioner = new Practitioner(null, request.getEmail(), passwordEncoder.encode(request.getPassword()));
                iPractitionerRepository.save(practitioner);
                return practitioner.toUserResponse();
            }

            case STAFF -> {
                Staff staff = new Staff(null, request.getEmail(), passwordEncoder.encode(request.getPassword()));
                iStaffRepository.save(staff);
                return staff.toUserResponse();
            }
            default -> throw new IlLegalUserRoleExeption(request.getRole());
        }
    }

    public AuthResponse loginUser(LoginRequest request){
        Optional<User> user = iUserRepository.findByEmail(request.getEmail());

        if(user.isEmpty()){
            throw new InvalidCredentialsException();
        }

        if(!passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
            throw new InvalidCredentialsException();
        }

        User authenticatedUser = user.get();
        UserResponse userResponse = authenticatedUser.toUserResponse();

        String token = jwtService.generateToken(
                authenticatedUser.getEmail(),
                userResponse.getRole().name()
        );

        return new AuthResponse(token, userResponse);
    }

    private boolean isEmailVerified(String email){
        return true;
    }

}