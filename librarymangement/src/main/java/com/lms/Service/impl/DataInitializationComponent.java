package com.lms.Service.impl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lms.Model.User;
import com.lms.domain.UserRole;
import com.lms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner{


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();

    }
    private void initializeAdminUser() {
    
    
        String adminEmail = "admin@gmail.com";
        String adminPassword = "admin123";

        if (userRepository.findByEmail(adminEmail) == null) {
         
            User user = User.builder()
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .fullName("ian quea")
                    .role(UserRole.ROLE_ADMIN)
                    .build();
            User admin = userRepository.save(user);
        }
    }
}
