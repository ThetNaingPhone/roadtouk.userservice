package com.example.roadtouk.userservice.controller;

import com.example.roadtouk.userservice.dto.UserDto;
import com.example.roadtouk.userservice.service.UserDetailsImpl;
import com.example.roadtouk.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Cast the principal to our custom UserDetailsImpl to access the full user object
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create a DTO to send back to the client
        UserDto userDto = new UserDto();
//        userDto.setId(userDetails.getId());
//        userDto.setUsername(userDetails.getUser().getUsername()); // Get username from the wrapped User entity
//        userDto.setEmail(userDetails.getUsername()); // getUsername() on UserDetailsImpl returns the email
//        userDto.setApplications(userDetails.getUser().getApplications());
//        userDto.setAdvice(userDetails.getUser().getAdvice());
//        userDto.setCreatedAt(userDetails.getUser().getCreatedAt());

        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto saveUser = userService.save(userDto);
        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

}
