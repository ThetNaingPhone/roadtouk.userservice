package com.example.roadtouk.userservice.service.impl;

import com.example.roadtouk.userservice.dto.UserDto;
import com.example.roadtouk.userservice.entity.User;
import com.example.roadtouk.userservice.repository.UserRepository;
import com.example.roadtouk.userservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return convertToDto(user);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id:" + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = converToEntity(userDto);
        user = userRepository.save(user);
        return convertToDto(user);
    }

    private User converToEntity(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}
