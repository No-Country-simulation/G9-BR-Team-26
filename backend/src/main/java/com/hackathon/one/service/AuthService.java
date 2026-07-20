package com.hackathon.one.service;

import com.hackathon.one.domain.Usuario;
import com.hackathon.one.dto.AuthResponse;
import com.hackathon.one.dto.LoginRequest;
import com.hackathon.one.dto.SignupRequest;
import com.hackathon.one.dto.UserResponse;
import com.hackathon.one.repository.UsuarioRepository;
import com.hackathon.one.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration}")
    private Long expiration;

    public UserResponse signup(SignupRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado"); // Idealmente criar uma exception customizada e um ControllerAdvice
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .build();

        usuario = usuarioRepository.save(usuario);

        return UserResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .criadoEm(usuario.getCriadoEm())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        String jwt = jwtUtil.generateToken(authentication.getName());

        return AuthResponse.builder()
                .accessToken(jwt)
                .tokenType("Bearer")
                .expiresIn(expiration / 1000) // em segundos
                .build();
    }
}
