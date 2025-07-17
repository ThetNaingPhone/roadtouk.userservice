package com.example.roadtouk.userservice.auth;

import com.example.roadtouk.userservice.dto.LoginRequest;
import com.example.roadtouk.userservice.dto.RegistrationRequest;
import com.example.roadtouk.userservice.entity.RefreshToken;
import com.example.roadtouk.userservice.entity.User;
import com.example.roadtouk.userservice.repository.RefreshTokenRepository;
import com.example.roadtouk.userservice.repository.UserRepository;
import com.example.roadtouk.userservice.service.RefreshTokenService;
import com.example.roadtouk.userservice.service.UserDetailsImpl;
import com.example.roadtouk.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
    private RefreshTokenRepository refreshTokenRepository;

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
                 .role("ROLE_USER")
                 .applications(new ArrayList<>())
                 .advice(new ArrayList<>())
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

        // ✅ Add this block to extract role
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        String role = userDetailsImpl.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");

        String accessToken = jwtUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        // ✅ Now return AuthResponse with role
        return new AuthResponse(accessToken, refreshToken.getToken(), role);
    }
}