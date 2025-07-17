package com.example.roadtouk.userservice.service;

import com.example.roadtouk.userservice.dto.UserDto;
import com.example.roadtouk.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);

    List<UserDto> findAll();

    UserDto findById(String id);

    void delete(String id);

    UserDto save(UserDto userDto);
}