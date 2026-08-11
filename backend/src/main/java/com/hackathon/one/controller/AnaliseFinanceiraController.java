package com.hackathon.one.controller;

import com.hackathon.one.dto.AnaliseFinanceiraHistoricoResponse;
import com.hackathon.one.dto.AnaliseFinanceiraRequest;
import com.hackathon.one.dto.AnaliseFinanceiraResponse;
import com.hackathon.one.dto.EvolucaoFinanceiraResponse;
import com.hackathon.one.dto.ErrorResponse;
import com.hackathon.one.service.AnaliseFinanceiraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            description = "Recebe renda, endividamento e frequência de poupança, e devolve perfil financeiro, score, resumo de gastos e recomendações. " +
                    "O usuário deve estar autenticado; as transações consideradas no resumo de gastos são as já cadastradas por ele.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Análise gerada e persistida com sucesso",
                    content = @Content(schema = @Schema(implementation = AnaliseFinanceiraResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (ex: renda negativa, campo obrigatório ausente)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token de autenticação ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
            description = "Retorna todas as análises financeiras já realizadas pelo usuário autenticado, da mais recente para a mais antiga.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de análises retornada com sucesso (pode ser vazia, se o usuário nunca fez uma análise)"),
            @ApiResponse(responseCode = "401", description = "Token de autenticação ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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

    // IMPORTANTE: essa rota precisa vir ANTES de "/{id}", senão o Spring
    // tentaria interpretar "evolucao" como se fosse um ID.
    @Operation(
            summary = "Evolução financeira",
            description = "Compara a análise financeira mais recente com a anterior, indicando tendência de melhora, piora ou estabilidade. " +
                    "Se o usuário tiver menos de 2 análises, os campos de comparação vêm nulos e a tendência é 'indisponivel'.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evolução calculada com sucesso",
                    content = @Content(schema = @Schema(implementation = EvolucaoFinanceiraResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token de autenticação ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário ainda não possui nenhuma análise financeira",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
            description = "Retorna os dados completos de uma análise financeira específica. Um usuário só pode acessar as próprias análises.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Análise encontrada e retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token de autenticação ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Análise não encontrada, ou pertence a outro usuário",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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