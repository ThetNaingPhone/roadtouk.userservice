package com.example.roadtouk.userservice.auth;

public interface AuthService {
    AuthResponse login(String email, String password);
}