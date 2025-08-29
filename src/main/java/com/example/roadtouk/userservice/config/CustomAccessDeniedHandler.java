package com.example.roadtouk.userservice.config;

import com.example.roadtouk.userservice.exception.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Handles 403 Forbidden
@Component
class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpServletResponse.SC_FORBIDDEN,
                "Forbidden",
                "You don’t have permission to access this resource",
                request.getRequestURI()
        );
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}
