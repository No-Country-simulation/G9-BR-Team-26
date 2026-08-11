package com.hackathon.one.controller;

import com.hackathon.one.dto.AnaliseFinanceiraHistoricoResponse;
import com.hackathon.one.dto.AnaliseFinanceiraRequest;
import com.hackathon.one.dto.AnaliseFinanceiraResponse;
import com.hackathon.one.dto.EvolucaoFinanceiraResponse;
import com.hackathon.one.service.AnaliseFinanceiraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analise-financeira")
@RequiredArgsConstructor
@Tag(name = "Análise Financeira", description = "Geração de perfil financeiro, recomendações, histórico e evolução")
public class AnaliseFinanceiraController {

    private final AnaliseFinanceiraService analiseFinanceiraService;

    // ─────────────────────────────────────────────────
    //  POST /analise-financeira
    // ─────────────────────────────────────────────────

    @Operation(
            summary = "Gerar análise financeira",
            description = "Recebe renda, endividamento e frequência de poupança, e devolve perfil financeiro, score, resumo de gastos e recomendações",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ResponseEntity<AnaliseFinanceiraResponse> analisar(
            @Valid @RequestBody AnaliseFinanceiraRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        AnaliseFinanceiraResponse response = analiseFinanceiraService.analisar(request, emailUsuario);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────────
    //  GET /analise-financeira/historico
    // ─────────────────────────────────────────────────

    @Operation(
            summary = "Histórico de análises",
            description = "Retorna todas as análises financeiras já realizadas pelo usuário autenticado",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/historico")
    public ResponseEntity<List<AnaliseFinanceiraHistoricoResponse>> historico(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        List<AnaliseFinanceiraHistoricoResponse> response = analiseFinanceiraService.listarHistorico(emailUsuario);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────────
    //  GET /analise-financeira/evolucao
    // ─────────────────────────────────────────────────

    // Compara a análise mais recente com a anterior, mostrando a evolução
    // do perfil financeiro e do nível de endividamento do usuário ao longo do tempo.
    // IMPORTANTE: essa rota precisa vir ANTES de "/{id}", senão o Spring
    // tentaria interpretar "evolucao" como se fosse um ID.
    @Operation(
            summary = "Evolução financeira",
            description = "Compara a análise financeira mais recente com a anterior, indicando tendência de melhora, piora ou estabilidade",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/evolucao")
    public ResponseEntity<EvolucaoFinanceiraResponse> evolucao(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        EvolucaoFinanceiraResponse response = analiseFinanceiraService.calcularEvolucao(emailUsuario);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────────
    //  GET /analise-financeira/{id}
    // ─────────────────────────────────────────────────

    @Operation(
            summary = "Detalhe de uma análise",
            description = "Retorna os dados completos de uma análise financeira específica",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public ResponseEntity<AnaliseFinanceiraHistoricoResponse> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        AnaliseFinanceiraHistoricoResponse response = analiseFinanceiraService.buscarPorId(id, emailUsuario);
        return ResponseEntity.ok(response);
    }
}