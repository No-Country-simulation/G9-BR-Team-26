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
     * Retorna os dados públicos de um usuário pelo seu ID, desde que o
     * solicitante seja o dono do recurso (email do token == email do usuário-alvo).
     * Usuários não podem consultar dados de outras contas.
     *
     * @param id identificador do usuário
     * @param emailAutenticado e-mail extraído do token JWT do requisitante
     * @return DTO com dados do usuário
     * @throws ResourceNotFoundException se o usuário não for encontrado ou não pertencer ao requisitante
     */
    @Transactional(readOnly = true)
    public UserResponse buscarPorId(Long id, String emailAutenticado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        validarPosse(usuario, emailAutenticado, id);
        return toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Remove a conta do usuário, desde que o solicitante seja o dono do recurso
     * (email do token == email do usuário-alvo). Segue o mesmo padrão de
     * validação de posse usado em {@link TransacaoService#deletar}.
     *
     * @param id identificador do usuário a ser removido
     * @param emailAutenticado e-mail extraído do token JWT do requisitante
     * @throws ResourceNotFoundException se o usuário não for encontrado ou não pertencer ao requisitante
     */
    @Transactional
    public void deletarSeProprio(Long id, String emailAutenticado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        validarPosse(usuario, emailAutenticado, id);
        usuarioRepository.delete(usuario);
    }

    // ─────────────────────────────────────────
    //  Validação de posse
    // ─────────────────────────────────────────

    /**
     * Garante que o usuário autenticado só possa operar sobre a própria conta.
     * Lança {@link ResourceNotFoundException} (em vez de um 403) para não revelar
     * a existência de contas alheias, seguindo o mesmo padrão usado em {@link TransacaoService}.
     */
    private void validarPosse(Usuario usuario, String emailAutenticado, Long id) {
        if (!usuario.getEmail().equalsIgnoreCase(emailAutenticado)) {
            throw new ResourceNotFoundException("Usuario", id);
        }
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
