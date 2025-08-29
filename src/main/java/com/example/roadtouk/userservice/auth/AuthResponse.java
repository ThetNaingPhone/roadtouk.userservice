package com.example.roadtouk.userservice.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AuthResponse {
    // Getters and setters (or use Lombok)
    @Setter
    private String accessToken;
    @Setter
    private String refreshToken;
    private String role;

    public AuthResponse(String accessToken, String refreshToken, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }

}
