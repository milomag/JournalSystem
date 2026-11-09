package com.journalsystem.backend.service;

import com.journalsystem.backend.exception.EmailAlreadyRegisteredException;
import com.journalsystem.backend.exception.InvalidCredentialsException;
import com.journalsystem.backend.repository.IUserRepository;
import com.journalsystem.backend.dto.request.LoginRequest;
import com.journalsystem.backend.dto.request.RegisterRequest;
import com.journalsystem.backend.dto.response.UserResponse;
import com.journalsystem.backend.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService{

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyRegisteredException();
        }

        User user = new User(
                null,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole()
        );

        User savedUser = userRepository.save(user);

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