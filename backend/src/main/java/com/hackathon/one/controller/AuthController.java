package com.hackathon.one.controller;

import com.hackathon.one.dto.AuthResponse;
import com.hackathon.one.dto.LoginRequest;
import com.hackathon.one.dto.SignupRequest;
import com.hackathon.one.dto.UserResponse;
import com.hackathon.one.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para cadastro e login do usuário")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Cadastrar usuário", description = "Cria uma nova conta para o usuário no sistema")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        try {
            UserResponse response = authService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            // Em um cenário real usariamos ControllerAdvice para gerenciar
            if ("Email já cadastrado".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Fazer login", description = "Autentica o usuário e retorna um access token JWT")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
