package com.example.roadtouk.userservice;

import com.example.roadtouk.userservice.entity.Admin;
import com.example.roadtouk.userservice.entity.AdminRole;
import com.example.roadtouk.userservice.repository.AdminRepository;
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
	public CommandLineRunner initAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (adminRepository.findByEmail("mgmg123@gmail.com").isEmpty()) {
				Admin admin = Admin.builder()
						.username("mgmg123")
						.email("mgmg123@gmail.com")
						.passwordHash(passwordEncoder.encode("123123123"))
						.role(AdminRole.SUPER_ADMIN)
						.build();
				Admin save = adminRepository.save(admin);
			}
		};
	}
}
