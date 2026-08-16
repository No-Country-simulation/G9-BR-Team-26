package com.hackathon.one.service;

import com.hackathon.one.domain.Usuario;
import com.hackathon.one.dto.UserResponse;
import com.hackathon.one.exception.ResourceNotFoundException;
import com.hackathon.one.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void shouldFindUserByEmailAndMapToResponse() {
        Usuario usuario = Usuario.builder()
                .id(10L)
                .nome("Ana")
                .email("ana@email.com")
                .criadoEm(LocalDateTime.of(2024, 1, 1, 12, 0))
                .build();

        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));

        UserResponse response = usuarioService.buscarPorEmail("ana@email.com");

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getNome()).isEqualTo("Ana");
        assertThat(response.getEmail()).isEqualTo("ana@email.com");
    }

    @Test
    void shouldThrowWhenSearchingUserByEmailThatDoesNotExist() {
        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorEmail("naoexiste@email.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario");
    }

    @Test
    void shouldListAllUsersAndMapThem() {
        Usuario usuario = Usuario.builder()
                .id(11L)
                .nome("Bia")
                .email("bia@email.com")
                .criadoEm(LocalDateTime.of(2024, 2, 2, 8, 0))
                .build();

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UserResponse> result = usuarioService.listarTodos();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNome()).isEqualTo("Bia");
    }

    @Test
    void shouldDeleteExistingUserWhenOwner() {
        Usuario usuario = Usuario.builder()
                .id(12L)
                .nome("Caio")
                .email("caio@email.com")
                .build();

        when(usuarioRepository.findById(12L)).thenReturn(Optional.of(usuario));

        usuarioService.deletarSeProprio(12L, "caio@email.com");

        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void shouldThrowWhenDeletingUserThatIsNotTheOwner() {
        Usuario usuario = Usuario.builder()
                .id(13L)
                .nome("Duda")
                .email("duda@email.com")
                .build();

        when(usuarioRepository.findById(13L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> usuarioService.deletarSeProprio(13L, "outra@email.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldFindUserByIdWhenOwner() {
        Usuario usuario = Usuario.builder()
                .id(14L)
                .nome("Elis")
                .email("elis@email.com")
                .criadoEm(LocalDateTime.of(2024, 3, 3, 9, 0))
                .build();

        when(usuarioRepository.findById(14L)).thenReturn(Optional.of(usuario));

        UserResponse response = usuarioService.buscarPorId(14L, "elis@email.com");

        assertThat(response.getNome()).isEqualTo("Elis");
    }

    @Test
    void shouldThrowWhenFindingUserByIdThatIsNotTheOwner() {
        Usuario usuario = Usuario.builder()
                .id(15L)
                .nome("Flavio")
                .email("flavio@email.com")
                .build();

        when(usuarioRepository.findById(15L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> usuarioService.buscarPorId(15L, "outra@email.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
