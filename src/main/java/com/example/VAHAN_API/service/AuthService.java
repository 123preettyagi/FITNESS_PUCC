package com.example.VAHAN_API.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.VAHAN_API.dto.LoginRequest;
import com.example.VAHAN_API.dto.LoginResponse;
import com.example.VAHAN_API.dto.RegisterRequest;
import com.example.VAHAN_API.entity.User;
import com.example.VAHAN_API.repository.UserRepository;
import com.example.VAHAN_API.security.JwtTokenProvider;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    
    public LoginResponse authenticateUser(LoginRequest loginRequest) 
    {
    	
       try {
    	   
    	Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = tokenProvider.generateToken(authentication);
        
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setLastLogin(new Date());
        userRepository.save(user);
        
        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        
        return response;
       }
       catch (Exception e) 
       {
           e.printStackTrace();  // << add this
           throw e;
       }
       
            
    }
    
   /* public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }
        
     /*   user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setIsActive(true);
        
        return userRepository.save(user); 
        
        
    } */

public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }
        
        // Map RegisterRequest to User
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setMobileNumber(registerRequest.getMobileNumber());
        user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : "USER");
        user.setIsActive(true);
        
        return userRepository.save(user);
    }
}