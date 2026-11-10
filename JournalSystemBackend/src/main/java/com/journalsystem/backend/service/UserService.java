package com.journalsystem.backend.service;

import com.journalsystem.backend.exception.EmailAlreadyRegisteredException;
import com.journalsystem.backend.exception.InvalidCredentialsException;
import com.journalsystem.backend.repository.IUserRepository;
import com.journalsystem.backend.repository.IPatientRepository;
import com.journalsystem.backend.dto.request.LoginRequest;
import com.journalsystem.backend.dto.request.RegisterRequest;
import com.journalsystem.backend.dto.response.UserResponse;
import com.journalsystem.backend.entity.User;
import com.journalsystem.backend.entity.Patient;
import com.journalsystem.backend.entity.Role;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService{

    private final IUserRepository userRepository;
    private final PatientService patientService;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository,
                       PatientService patientService,
                       PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.patientService = patientService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse registerUser(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyRegisteredException();
        }

        if(request.getRole() == Role.PATIENT){
            if(request.getFullName() == null || request.getFullName().isBlank()
                    || request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()
                    || request.getDateOfBirth() == null){
                throw new IllegalArgumentException("All patient information is required.");
            }
        }

        User user = new User(
                null,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole(),
                null
        );

        User savedUser = userRepository.save(user);

        if(request.getRole() == Role.PATIENT){
            Patient patient = new Patient(
                    savedUser,
                    request.getEmail(),
                    request.getPhoneNumber(),
                    request.getFullName(),
                    request.getDateOfBirth()
            );

            patientService.createPatient(patient);
        }

        return toUserResponse(savedUser);
    }

    public UserResponse loginUser(LoginRequest request){
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isEmpty()){
            throw new InvalidCredentialsException();
        }

        if(!passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
            throw new InvalidCredentialsException();
        }

        return toUserResponse(user.get());
    }

    private UserResponse toUserResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}