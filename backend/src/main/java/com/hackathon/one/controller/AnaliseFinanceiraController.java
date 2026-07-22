package com.hackathon.one.controller;

import com.hackathon.one.dto.AnaliseFinanceiraHistoricoResponse;
import com.hackathon.one.dto.AnaliseFinanceiraRequest;
import com.hackathon.one.dto.AnaliseFinanceiraResponse;
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
@Tag(name = "Análise Financeira", description = "Geração de perfil financeiro, recomendações e histórico de análises")
public class AnaliseFinanceiraController {

    private final AnaliseFinanceiraService analiseFinanceiraService;

    // ─────────────────────────────────────────────────
    //  POST /analise-financeira
    // ─────────────────────────────────────────────────

    @Operation(
            summary = "Gerar análise financeira",
            description = "Recebe renda, endividamento e frequência de poupança, e devolve perfil financeiro, resumo de gastos e recomendações",
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

    // Lista todas as análises já feitas pelo usuário autenticado, da mais recente para a mais antiga.
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
    //  GET /analise-financeira/{id}
    // ─────────────────────────────────────────────────

    // Retorna o detalhe de uma análise específica do usuário autenticado.
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