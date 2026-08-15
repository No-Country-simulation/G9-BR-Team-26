package com.hackathon.one.controller;

import com.hackathon.one.dto.ErrorResponse;
import com.hackathon.one.dto.SimulacaoQuitacaoRequest;
import com.hackathon.one.dto.SimulacaoQuitacaoResponse;
import com.hackathon.one.service.SimulacaoQuitacaoService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulacao")
@RequiredArgsConstructor
@Tag(name = "Simulação", description = "Simulações financeiras auxiliares")
public class SimulacaoQuitacaoController {

    private final SimulacaoQuitacaoService simulacaoQuitacaoService;

    @Operation(
            summary = "Simular quitação de dívida",
            description = "Calcula quantos meses seriam necessários para quitar uma dívida, dado um valor mensal hipotético. " +
                    "A taxa de juros mensal é opcional: se omitida, o cálculo é feito sem juros (divisão simples). " +
                    "Se informada, usa a fórmula de Price (juros compostos, prestação fixa).",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Simulação calculada com sucesso",
                    content = @Content(schema = @Schema(implementation = SimulacaoQuitacaoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos, ou o valor mensal informado não é suficiente " +
                    "sequer para cobrir os juros da dívida (a dívida nunca seria quitada)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token de autenticação ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/quitacao")
    public ResponseEntity<SimulacaoQuitacaoResponse> simular(
            @Valid @RequestBody SimulacaoQuitacaoRequest request
    ) {
        SimulacaoQuitacaoResponse response = simulacaoQuitacaoService.simular(request);
        return ResponseEntity.ok(response);
    }
}