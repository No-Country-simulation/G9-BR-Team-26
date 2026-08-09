package com.hackathon.one.service;

import com.hackathon.one.domain.AnaliseFinanceira;
import com.hackathon.one.domain.MetaFinanceira;
import com.hackathon.one.domain.Usuario;
import com.hackathon.one.dto.MetaFinanceiraRequest;
import com.hackathon.one.dto.MetaFinanceiraResponse;
import com.hackathon.one.exception.ResourceNotFoundException;
import com.hackathon.one.repository.AnaliseFinanceiraRepository;
import com.hackathon.one.repository.MetaFinanceiraRepository;
import com.hackathon.one.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Metas financeiras: o usuário define um nível de endividamento desejado
 * e uma data limite. O progresso é calculado comparando o endividamento
 * ATUAL (última análise) com o endividamento INICIAL (capturado no momento
 * da criação da meta) e o alvo — isso mantém o progresso estável, sem
 * oscilar caso o endividamento piore entre duas análises.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MetaFinanceiraService {

    private final MetaFinanceiraRepository metaFinanceiraRepository;
    private final AnaliseFinanceiraRepository analiseFinanceiraRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public MetaFinanceiraResponse criar(MetaFinanceiraRequest request, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", emailUsuario));

        Integer endividamentoAtual = buscarEndividamentoAtual(usuario.getId());

        MetaFinanceira meta = MetaFinanceira.builder()
                .usuario(usuario)
                .descricao(request.descricao())
                .endividamentoAlvo(request.endividamentoAlvo())
                .endividamentoInicial(endividamentoAtual) // capturado no momento da criação
                .dataAlvo(request.dataAlvo())
                .concluida(false)
                .build();

        metaFinanceiraRepository.save(meta);

        log.info("Meta financeira criada para usuário: {} | alvo: {}% | inicial: {}%",
                emailUsuario, request.endividamentoAlvo(), endividamentoAtual);

        return toResponse(meta, endividamentoAtual);
    }

    @Transactional(readOnly = true)
    public List<MetaFinanceiraResponse> listar(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", emailUsuario));

        Integer endividamentoAtual = buscarEndividamentoAtual(usuario.getId());

        return metaFinanceiraRepository.findByUsuarioIdOrderByCriadoEmDesc(usuario.getId())
                .stream()
                .map(meta -> toResponse(meta, endividamentoAtual))
                .toList();
    }

    private Integer buscarEndividamentoAtual(Long usuarioId) {
        List<AnaliseFinanceira> analises = analiseFinanceiraRepository
                .findByUsuarioIdOrderByCriadoEmDesc(usuarioId);
        return analises.isEmpty() ? null : analises.get(0).getNivelEndividamento();
    }

    // Progresso calculado com base no endividamento INICIAL (capturado na criação
    // da meta), não no "atual" a cada chamada — assim o percentual só aumenta
    // conforme o usuário realmente se aproxima do alvo, sem oscilar para trás
    // por causa de uma análise pontualmente pior.
    private Double calcularProgresso(Integer endividamentoInicial, Integer endividamentoAtual, Integer endividamentoAlvo) {
        if (endividamentoAtual == null) {
            return null;
        }
        // Se não havia análise no momento da criação da meta, usa o atual como base
        // (fallback, evita divisão por dado ausente).
        int base = endividamentoInicial != null ? endividamentoInicial : endividamentoAtual;

        if (endividamentoAtual <= endividamentoAlvo) {
            return 100.0;
        }
        if (base <= endividamentoAlvo) {
            // Base já era igual/menor que o alvo (situação rara/inconsistente) — evita divisão por zero.
            return 100.0;
        }

        double progresso = ((double) (base - endividamentoAtual) / (base - endividamentoAlvo)) * 100.0;
        return Math.max(0.0, Math.min(100.0, progresso));
    }

    private MetaFinanceiraResponse toResponse(MetaFinanceira meta, Integer endividamentoAtual) {
        Double progresso = calcularProgresso(
                meta.getEndividamentoInicial(),
                endividamentoAtual,
                meta.getEndividamentoAlvo()
        );

        return new MetaFinanceiraResponse(
                meta.getId(),
                meta.getDescricao(),
                meta.getEndividamentoAlvo(),
                endividamentoAtual,
                meta.getDataAlvo(),
                meta.getCriadoEm(),
                meta.getConcluida(),
                progresso
        );
    }
}