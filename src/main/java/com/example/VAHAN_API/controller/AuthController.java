package com.example.VAHAN_API.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.VAHAN_API.dto.ApiResponse;
import com.example.VAHAN_API.dto.LoginRequest;
import com.example.VAHAN_API.dto.LoginResponse;
import com.example.VAHAN_API.dto.RegisterRequest;
import com.example.VAHAN_API.entity.User;
import com.example.VAHAN_API.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ========== Authentication Endpoints ==========

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(
            @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Login successful", response)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Invalid credentials"));
        }
    }

  /*  @PostMapping("register")
    public ResponseEntity<ApiResponse> registerUser(
            @RequestBody User user) {
        try {
            User registeredUser = authService.registerUser(user);
            return ResponseEntity.ok(
                    new ApiResponse(true, "User registered successfully", registeredUser)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    } */
    
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = authService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "User registered successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

}