package com.hackathon.one.service;

import com.hackathon.one.domain.Transacao;
import com.hackathon.one.domain.Usuario;
import com.hackathon.one.dto.TransacaoRequest;
import com.hackathon.one.dto.TransacaoResponse;
import com.hackathon.one.exception.ResourceNotFoundException;
import com.hackathon.one.repository.TransacaoRepository;
import com.hackathon.one.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.requireNonNull;

// Regras de negócio para transações financeiras.
// O {@code emailUsuario} passado nos métodos é sempre extraído do token JWT pelo Controller,
// garantindo que um usuário só opera sobre seus próprios dados.
@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;

    // ─────────────────────────────────────────
    //  Criação
    // ─────────────────────────────────────────

    @Transactional
    public TransacaoResponse criar(TransacaoRequest request, String emailUsuario) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", emailUsuario));

        // A categoria não é recebida pelo cliente neste momento.
        // Quando o modelo de classificação estiver pronto, ele será responsável por definir a categoria correta.
        String categoria = "outros";

        Transacao transacao = Transacao.builder()
                .descricao(request.descricao())
                .valor(request.valor())
                .categoria(categoria)
                .usuario(usuario)
                .build();

        Transacao salva = transacaoRepository.save(transacao);

        return toResponse(salva);
    }

    // ─────────────────────────────────────────
    //  Consulta
    // ─────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<TransacaoResponse> listarMinhas(String emailUsuario) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", emailUsuario));

        return transacaoRepository
                .findByUsuarioIdOrderByCriadoEmDesc(usuario.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TransacaoResponse atualizar(Long id, TransacaoRequest request, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", emailUsuario));

        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transacao", id));

        if (!transacao.getUsuario().getId().equals(usuario.getId())) {
            throw new ResourceNotFoundException("Transacao", id);
        }

        transacao.setDescricao(requireNonNull(request.descricao(), "A descrição é obrigatória."));
        transacao.setValor(requireNonNull(request.valor(), "O valor é obrigatório."));
        // A categoria não é recebida pelo cliente neste momento.
        // Quando o modelo de classificação estiver pronto, ele será responsável por definir a categoria correta.
        transacao.setCategoria("outros");

        Transacao atualizada = transacaoRepository.save(transacao);
        return toResponse(atualizada);
    }

    @Transactional
    public void deletar(Long id, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", emailUsuario));

        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transacao", id));

        if (!transacao.getUsuario().getId().equals(usuario.getId())) {
            throw new ResourceNotFoundException("Transacao", id);
        }

        transacaoRepository.delete(transacao);
    }

    // ─────────────────────────────────────────
    //  Mapeamento manual Entity → DTO
    // ─────────────────────────────────────────

    private TransacaoResponse toResponse(Transacao transacao) {
        return new TransacaoResponse(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getCategoria(),
                transacao.getUsuario().getId(),
                transacao.getCriadoEm()
        );
    }
}
