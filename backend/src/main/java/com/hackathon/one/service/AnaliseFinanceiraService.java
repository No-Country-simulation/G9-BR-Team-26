package com.hackathon.one.service;

import com.hackathon.one.domain.AnaliseFinanceira;
import com.hackathon.one.domain.Transacao;
import com.hackathon.one.domain.Usuario;
import com.hackathon.one.dto.AnaliseFinanceiraHistoricoResponse;
import com.hackathon.one.dto.AnaliseFinanceiraRequest;
import com.hackathon.one.dto.AnaliseFinanceiraResponse;
import com.hackathon.one.exception.ResourceNotFoundException;
import com.hackathon.one.repository.AnaliseFinanceiraRepository;
import com.hackathon.one.repository.TransacaoRepository;
import com.hackathon.one.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Regras de negócio para análise financeira.
 * ATENÇÃO: o cálculo de perfil/probabilidade/recomendações está MOCKADO
 * enquanto o modelo real do time de Data Science não é integrado (ver card
 * "Integração real com API Python" no board — bloqueado até o Bloco 5 de DS).
 * O resumo de gastos, por outro lado, já é calculado com dados reais do banco.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnaliseFinanceiraService {

    private final AnaliseFinanceiraRepository analiseFinanceiraRepository;
    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public AnaliseFinanceiraResponse analisar(AnaliseFinanceiraRequest request, String emailUsuario) {
        log.info("Iniciando análise financeira para usuário: {}", emailUsuario);

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", emailUsuario));

        List<Transacao> transacoes = transacaoRepository
                .findByUsuarioIdOrderByCriadoEmDesc(usuario.getId());

        Map<String, BigDecimal> resumoGastos = transacoes.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategoria() != null ? t.getCategoria() : "outros",
                        Collectors.reducing(BigDecimal.ZERO, Transacao::getValor, BigDecimal::add)
                ));

        // ── MOCK a partir daqui ──────────────────────────────────────
        String perfilFinanceiro = calcularPerfilMock(request);
        Double probabilidade = 0.75;
        List<String> recomendacoes = gerarRecomendacoesMock(perfilFinanceiro);
        // ── fim do MOCK ──────────────────────────────────────────────

        AnaliseFinanceira analise = AnaliseFinanceira.builder()
                .usuario(usuario)
                .rendaMensal(request.rendaMensal())
                .nivelEndividamento(request.nivelEndividamento())
                .frequenciaPoupanca(request.frequenciaPoupanca())
                .perfilFinanceiro(perfilFinanceiro)
                .probabilidade(probabilidade)
                .build();

        recomendacoes.forEach(analise::adicionarRecomendacao);

        analiseFinanceiraRepository.save(analise);

        log.info("Análise financeira concluída para usuário: {} | perfil: {}", emailUsuario, perfilFinanceiro);

        return new AnaliseFinanceiraResponse(
                perfilFinanceiro,
                probabilidade,
                resumoGastos,
                recomendacoes
        );
    }

    // ─────────────────────────────────────────────────
    //  Histórico
    // ─────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<AnaliseFinanceiraHistoricoResponse> listarHistorico(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", emailUsuario));

        return analiseFinanceiraRepository
                .findByUsuarioIdOrderByCriadoEmDesc(usuario.getId())
                .stream()
                .map(this::toHistoricoResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AnaliseFinanceiraHistoricoResponse buscarPorId(Long id, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", emailUsuario));

        AnaliseFinanceira analise = analiseFinanceiraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AnaliseFinanceira", id));

        // Garante que o usuário só acessa as próprias análises
        // (mesmo padrão de segurança usado em TransacaoService).
        if (!analise.getUsuario().getId().equals(usuario.getId())) {
            throw new ResourceNotFoundException("AnaliseFinanceira", id);
        }

        return toHistoricoResponse(analise);
    }

    // ─────────────────────────────────────────────────
    //  Mapeamento
    // ─────────────────────────────────────────────────

    private AnaliseFinanceiraHistoricoResponse toHistoricoResponse(AnaliseFinanceira analise) {
        List<String> textosRecomendacoes = analise.getRecomendacoes().stream()
                .map(r -> r.getTexto())
                .toList();

        return new AnaliseFinanceiraHistoricoResponse(
                analise.getId(),
                analise.getCriadoEm(),
                analise.getRendaMensal(),
                analise.getNivelEndividamento(),
                analise.getFrequenciaPoupanca(),
                analise.getPerfilFinanceiro(),
                analise.getProbabilidade(),
                textosRecomendacoes
        );
    }

    // ── Métodos privados de mock — serão REMOVIDOS quando a integração
    //    real com a API Python (Data Science) estiver pronta ──────────

    private String calcularPerfilMock(AnaliseFinanceiraRequest request) {
        if (request.nivelEndividamento() > 50) {
            return "Em risco";
        } else if (request.nivelEndividamento() > 20) {
            return "Em observacao";
        }
        return "Saudavel";
    }

    private List<String> gerarRecomendacoesMock(String perfil) {
        return switch (perfil) {
            case "Em risco" -> List.of(
                    "Priorizar a quitação de dívidas com juros mais altos",
                    "Revisar gastos não essenciais imediatamente"
            );
            case "Em observacao" -> List.of(
                    "Monitorar gastos recorrentes de entretenimento",
                    "Aumentar reserva financeira mensal"
            );
            default -> List.of(
                    "Manter o padrão atual de poupança",
                    "Considerar investimentos de longo prazo"
            );
        };
    }
}