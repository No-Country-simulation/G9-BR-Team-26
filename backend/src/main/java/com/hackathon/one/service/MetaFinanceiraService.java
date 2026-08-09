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
 * da análise financeira mais recente com o alvo da meta.
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

        MetaFinanceira meta = MetaFinanceira.builder()
                .usuario(usuario)
                .descricao(request.descricao())
                .endividamentoAlvo(request.endividamentoAlvo())
                .dataAlvo(request.dataAlvo())
                .concluida(false)
                .build();

        metaFinanceiraRepository.save(meta);

        log.info("Meta financeira criada para usuário: {} | alvo: {}%", emailUsuario, request.endividamentoAlvo());

        return toResponse(meta, buscarEndividamentoAtual(usuario.getId()));
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

    // Busca o endividamento da análise mais recente do usuário, se houver.
    private Integer buscarEndividamentoAtual(Long usuarioId) {
        List<AnaliseFinanceira> analises = analiseFinanceiraRepository
                .findByUsuarioIdOrderByCriadoEmDesc(usuarioId);
        return analises.isEmpty() ? null : analises.get(0).getNivelEndividamento();
    }

    // Calcula o progresso percentual em direção à meta.
    // Retorna null se o usuário ainda não fez nenhuma análise.
    private Double calcularProgresso(Integer endividamentoAtual, Integer endividamentoAlvo) {
        if (endividamentoAtual == null) {
            return null;
        }
        if (endividamentoAtual <= endividamentoAlvo) {
            return 100.0;
        }
        // Quanto mais perto do alvo, maior o progresso. Base de comparação: o próprio valor atual.
        double progresso = 100.0 - ((double) (endividamentoAtual - endividamentoAlvo) / endividamentoAtual * 100.0);
        return Math.max(0.0, Math.min(100.0, progresso));
    }

    private MetaFinanceiraResponse toResponse(MetaFinanceira meta, Integer endividamentoAtual) {
        Double progresso = calcularProgresso(endividamentoAtual, meta.getEndividamentoAlvo());

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