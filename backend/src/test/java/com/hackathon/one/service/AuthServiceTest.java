package com.hackathon.one.service;

import com.hackathon.one.domain.Usuario;
import com.hackathon.one.dto.LoginRequest;
import com.hackathon.one.dto.SignupRequest;
import com.hackathon.one.dto.UserResponse;
import com.hackathon.one.repository.UsuarioRepository;
import com.hackathon.one.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(usuarioRepository, passwordEncoder, authenticationManager, jwtUtil);
        ReflectionTestUtils.setField(authService, "expiration", 3600000L);
    }

    @Test
    void shouldThrowWhenSignupEmailAlreadyExists() {
        SignupRequest request = new SignupRequest();
        request.setNome("Maria");
        request.setEmail("maria@email.com");
        request.setSenha("123456");
        when(usuarioRepository.existsByEmail("maria@email.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email já cadastrado");
    }

    @Test
    void shouldCreateUserAndReturnPublicDataOnSignup() {
        SignupRequest request = new SignupRequest();
        request.setNome("Maria");
        request.setEmail("maria@email.com");
        request.setSenha("123456");
        Usuario savedUser = Usuario.builder()
                .id(7L)
                .nome("Maria")
                .email("maria@email.com")
                .senha("encoded")
                .build();

        when(usuarioRepository.existsByEmail("maria@email.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(savedUser);

        UserResponse response = authService.signup(request);

        assertThat(response.getId()).isEqualTo(7L);
        assertThat(response.getNome()).isEqualTo("Maria");
        assertThat(response.getEmail()).isEqualTo("maria@email.com");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void shouldGenerateJwtOnLogin() {
        LoginRequest request = new LoginRequest();
        request.setEmail("maria@email.com");
        request.setSenha("123456");
        Authentication authentication = new UsernamePasswordAuthenticationToken("maria@email.com", "secret");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtil.generateToken("maria@email.com")).thenReturn("token-123");

        var response = authService.login(request);

        assertThat(response.getAccessToken()).isEqualTo("token-123");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiresIn()).isEqualTo(3600L);
    }
}
