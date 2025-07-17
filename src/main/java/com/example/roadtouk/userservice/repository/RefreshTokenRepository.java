package com.example.roadtouk.userservice.repository;
import com.example.roadtouk.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.roadtouk.userservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}

