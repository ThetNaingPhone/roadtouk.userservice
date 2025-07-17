package com.example.roadtouk.userservice.auth;

import com.example.roadtouk.userservice.dto.LoginRequest;
import com.example.roadtouk.userservice.dto.RegistrationRequest;
import com.example.roadtouk.userservice.exception.TokenRefreshException;
import com.example.roadtouk.userservice.dto.TokenRefreshRequest;
import com.example.roadtouk.userservice.entity.RefreshToken;
import com.example.roadtouk.userservice.service.RefreshTokenService;
import com.example.roadtouk.userservice.service.UserDetailsImpl;
import com.example.roadtouk.userservice.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        authService.registerUser(registrationRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetailsImpl userDetails = new UserDetailsImpl(user);
                    String newAccessToken = jwtUtil.generateToken(userDetails);
                    String role = userDetails.getAuthorities().stream()
                            .findFirst()
                            .map(GrantedAuthority::getAuthority)
                            .orElse("ROLE_USER");
                    return ResponseEntity.ok(new AuthResponse(newAccessToken, requestRefreshToken, role));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }
}
