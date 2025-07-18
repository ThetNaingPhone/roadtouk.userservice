package com.example.roadtouk.userservice;

import com.example.roadtouk.userservice.entity.Role;
import com.example.roadtouk.userservice.entity.User;
import com.example.roadtouk.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Check if the admin user already exists by email or username
			if (userRepository.findByEmail("mgmg123@gmail.com").isEmpty() && userRepository.findByUsername("mgmg123").isEmpty()) {
				User admin = User.builder()
						.username("mgmg123")
						.email("mgmg123@gmail.com")
						.passwordHash(passwordEncoder.encode("123123123"))
						.role(Role.ROLE_ADMIN) // Assign the admin role
						.build();
				userRepository.save(admin);
			}
		};
	}
}