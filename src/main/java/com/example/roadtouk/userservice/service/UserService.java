package com.example.roadtouk.userservice.service;
import com.example.roadtouk.userservice.dto.UserDto;
import com.example.roadtouk.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User user);
    User create(UserDto dto);
    List<User> getAllUsers();
    Optional<User> getUserByEmail(String email);
}