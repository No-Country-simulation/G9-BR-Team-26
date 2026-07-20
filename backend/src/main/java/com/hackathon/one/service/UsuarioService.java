package com.hackathon.one.service;

import com.hackathon.one.domain.Usuario;
import com.hackathon.one.dto.UserResponse;
import com.hackathon.one.exception.ResourceNotFoundException;
import com.hackathon.one.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Regras de negócio para gerenciamento de usuários.
 * Operações de autenticação (signup/login) permanecem em {@link AuthService}.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // ─────────────────────────────────────────
    //  Consulta por e-mail (para uso com JWT)
    // ─────────────────────────────────────────

    @Transactional(readOnly = true)
    public UserResponse buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", email));
        return toResponse(usuario);
    }

    // ─────────────────────────────────────────
    //  Consulta por ID
    // ─────────────────────────────────────────

    /**
     * Retorna os dados públicos de um usuário pelo seu ID.
     * A senha nunca é exposta — o retorno é sempre via DTO.
     *
     * @param id identificador do usuário
     * @return DTO com dados do usuário
     * @throws ResourceNotFoundException se o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public UserResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        usuarioRepository.delete(usuario);
    }

    // ─────────────────────────────────────────
    //  Mapeamento manual Entity → DTO
    // ─────────────────────────────────────────

    /**
     * Converte uma entidade {@link Usuario} para seu DTO de resposta.
     * A senha é deliberadamente omitida do mapeamento.
     */
    private UserResponse toResponse(Usuario usuario) {
        return UserResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .criadoEm(usuario.getCriadoEm())
                .build();
    }
}
