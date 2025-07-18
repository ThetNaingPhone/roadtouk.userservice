package com.example.roadtouk.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Changed to Long for consistency

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id") // This now correctly joins on the Long ID
    private User user;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}