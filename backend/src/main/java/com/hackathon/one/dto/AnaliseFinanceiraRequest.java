package com.hackathon.one.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

// Dados enviados pelo cliente para solicitar uma análise financeira.
// O usuário dono da análise é identificado pelo token JWT — nunca pelo body.
@Schema(name = "AnaliseFinanceiraRequest", description = "Payload para solicitar uma análise financeira")
public record AnaliseFinanceiraRequest(

        @Schema(example = "4500", description = "Renda mensal do usuário")
        @NotNull(message = "A renda mensal é obrigatória.")
        @Positive(message = "A renda mensal deve ser positiva.")
        BigDecimal rendaMensal,

        @Schema(example = "25", description = "Nível de endividamento (percentual, 0 a 100)")
        @NotNull(message = "O nível de endividamento é obrigatório.")
        @PositiveOrZero(message = "O nível de endividamento não pode ser negativo.")
        Integer nivelEndividamento,

        @Schema(example = "Media", description = "Frequência de poupança: Baixa, Media ou Alta")
        @NotBlank(message = "A frequência de poupança é obrigatória.")
        String frequenciaPoupanca

) {}