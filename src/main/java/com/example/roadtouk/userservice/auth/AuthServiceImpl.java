package com.example.roadtouk.userservice.auth;

import com.example.roadtouk.userservice.entity.RefreshToken;
import com.example.roadtouk.userservice.entity.User;
import com.example.roadtouk.userservice.repository.RefreshTokenRepository;
import com.example.roadtouk.userservice.repository.UserRepository;
import com.example.roadtouk.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEmail(user.getEmail());
        refreshToken.setUser(user); 
        refreshToken.setToken(java.util.UUID.randomUUID().toString());
        refreshToken.setExpiryDate(java.time.Instant.now().plusSeconds(60 * 60 * 24 * 7)); // 7 days
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail());
        RefreshToken refreshToken = createRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken.getToken());
    }
}