package com.example.roadtouk.userservice.auth;

import lombok.Getter;

@Getter
public class AuthResponse {
    // Getters and setters (or use Lombok)
    private String accessToken;
    private String refreshToken;
    private String role;

    public AuthResponse(String accessToken, String refreshToken, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
