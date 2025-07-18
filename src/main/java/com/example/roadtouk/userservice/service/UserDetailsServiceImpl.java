package com.example.roadtouk.userservice.service;

import com.example.roadtouk.userservice.entity.User;
import com.example.roadtouk.userservice.repository.AdminRepository;
import com.example.roadtouk.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseGet(() -> adminRepository.findByEmail(email)
                        .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email" + email)));
    }
}
