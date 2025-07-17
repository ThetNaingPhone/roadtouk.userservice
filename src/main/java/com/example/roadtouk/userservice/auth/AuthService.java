package com.example.roadtouk.userservice.auth;

import com.example.roadtouk.userservice.dto.LoginRequest;
import com.example.roadtouk.userservice.dto.RegistrationRequest;
import com.example.roadtouk.userservice.entity.User;

public interface AuthService {
    User registerUser(RegistrationRequest registrationRequest);
    AuthResponse login(LoginRequest loginRequest);
}