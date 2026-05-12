package com.storyhub.controller;

import com.storyhub.dto.request.LoginRequest;
import com.storyhub.dto.request.RegistroRequest;
import com.storyhub.dto.response.LoginResponse;
import com.storyhub.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponse> registro(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(201).body(authService.registro(request));
    }
}