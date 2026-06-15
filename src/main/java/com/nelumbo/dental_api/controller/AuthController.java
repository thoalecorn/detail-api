package com.nelumbo.dental_api.controller;

import com.nelumbo.dental_api.dto.auth.LoginRequest;
import com.nelumbo.dental_api.dto.auth.LoginResponse;
import com.nelumbo.dental_api.service.AuthService;
import com.nelumbo.dental_api.dto.auth.RegisterRequest;
import com.nelumbo.dental_api.dto.auth.UserResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring(7);
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/{id}/clinics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> associateClinics(@PathVariable Long id,
                                                        @RequestBody List<Long> clinicIds) {
        return ResponseEntity.ok(authService.associateClinics(id, clinicIds));
    }
}