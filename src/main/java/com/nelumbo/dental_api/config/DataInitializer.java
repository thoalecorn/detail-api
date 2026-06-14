package com.nelumbo.dental_api.config;

import com.nelumbo.dental_api.entity.User;
import com.nelumbo.dental_api.enums.Role;
import com.nelumbo.dental_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@mail.com").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@mail.com")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
            System.out.println(">>> Usuario ADMIN creado correctamente");
        }
    }
}