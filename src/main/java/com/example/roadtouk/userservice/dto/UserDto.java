package com.example.roadtouk.userservice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {
    private String id;
    private String username;
    private String email;
    private List<String> applications;
    private List<String> advice;
    private LocalDateTime createdAt;
}
