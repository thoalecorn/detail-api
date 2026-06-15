package com.nelumbo.dental_api.service;

import com.nelumbo.dental_api.dto.auth.LoginRequest;
import com.nelumbo.dental_api.dto.auth.LoginResponse;
import com.nelumbo.dental_api.dto.auth.RegisterRequest;
import com.nelumbo.dental_api.dto.auth.UserResponse;
import com.nelumbo.dental_api.entity.Clinic;
import com.nelumbo.dental_api.entity.User;
import com.nelumbo.dental_api.entity.UserClinic;
import com.nelumbo.dental_api.enums.Role;
import com.nelumbo.dental_api.repository.ClinicRepository;
import com.nelumbo.dental_api.repository.UserClinicRepository;
import com.nelumbo.dental_api.repository.UserRepository;
import com.nelumbo.dental_api.security.JwtUtil;
import com.nelumbo.dental_api.security.TokenBlacklist;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserClinicRepository userClinicRepository;
    private final ClinicRepository clinicRepository;
    private final TokenBlacklist tokenBlacklist;

    @Value("${jwt.expiration}")
    private Long expiration;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponse(token, expiration);
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.RECEPCIONISTA)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        if (request.getClinicIds() != null && !request.getClinicIds().isEmpty()) {
            for (Long clinicId : request.getClinicIds()) {
                Clinic clinic = clinicRepository.findById(clinicId)
                        .orElseThrow(() -> new RuntimeException(
                                "Clínica no encontrada: " + clinicId));
                UserClinic userClinic = UserClinic.builder()
                        .user(user)
                        .clinic(clinic)
                        .build();
                userClinicRepository.save(userClinic);
            }
        }

        return new UserResponse(user.getId(), user.getUsername(),
                user.getEmail(), user.getRole().name());
    }

    public void logout(String token) {
        tokenBlacklist.add(token);
    }
}