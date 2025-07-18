package com.example.roadtouk.userservice.auth;

import com.example.roadtouk.userservice.dto.LoginRequest;
import com.example.roadtouk.userservice.dto.RegistrationRequest;
import com.example.roadtouk.userservice.entity.RefreshToken;
import com.example.roadtouk.userservice.entity.Role;
import com.example.roadtouk.userservice.entity.User;
import com.example.roadtouk.userservice.repository.UserRepository;
import com.example.roadtouk.userservice.service.RefreshTokenService;
import com.example.roadtouk.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public User registerUser(RegistrationRequest registrationRequest) {
        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = User.builder()
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .passwordHash(passwordEncoder.encode(registrationRequest.getPassword()))
                .role(Role.ROLE_USER) // Default role for new registrations
                .build();
        return userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User has no role."))
                .getAuthority();

        String accessToken = jwtUtil.generateToken(userDetails);

        // This now works for both users and admins because we only search the UserRepository
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new AuthResponse(accessToken, refreshToken.getToken(), role);
    }
}
