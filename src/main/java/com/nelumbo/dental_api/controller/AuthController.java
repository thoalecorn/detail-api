package com.nelumbo.dental_api.controller;

import com.nelumbo.dental_api.dto.auth.LoginRequest;
import com.nelumbo.dental_api.dto.auth.LoginResponse;
import com.nelumbo.dental_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}