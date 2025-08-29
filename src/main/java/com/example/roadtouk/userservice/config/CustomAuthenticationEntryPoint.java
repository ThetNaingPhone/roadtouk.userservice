package com.example.roadtouk.userservice.config;

import com.example.roadtouk.userservice.exception.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Handles 401 Unauthorized
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "Invalid or missing credentials",
                request.getRequestURI()
        );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}

