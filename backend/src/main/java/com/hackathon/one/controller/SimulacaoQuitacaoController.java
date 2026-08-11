package com.hackathon.one.controller;

import com.hackathon.one.dto.SimulacaoQuitacaoRequest;
import com.hackathon.one.dto.SimulacaoQuitacaoResponse;
import com.hackathon.one.service.SimulacaoQuitacaoService;
import io.swagger.v3.oas.annotations.Operation;
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
            description = "Calcula quantos meses seriam necessários para quitar uma dívida, dado um valor mensal hipotético",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/quitacao")
    public ResponseEntity<SimulacaoQuitacaoResponse> simular(
            @Valid @RequestBody SimulacaoQuitacaoRequest request
    ) {
        SimulacaoQuitacaoResponse response = simulacaoQuitacaoService.simular(request);
        return ResponseEntity.ok(response);
    }
}