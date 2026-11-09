package com.journalsystem.backend.controller;

import com.journalsystem.backend.dto.request.LoginRequest;
import com.journalsystem.backend.dto.request.RegisterRequest;
import com.journalsystem.backend.dto.response.UserResponse;
import com.journalsystem.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController{

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request){
        UserResponse response = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody LoginRequest request){
        UserResponse response = userService.loginUser(request);

        return ResponseEntity.ok(response);
    }
}