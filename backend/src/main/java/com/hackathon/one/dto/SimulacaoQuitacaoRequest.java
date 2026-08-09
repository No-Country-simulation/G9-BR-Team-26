package com.hackathon.one.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(name = "SimulacaoQuitacaoRequest", description = "Payload para simular a quitação de uma dívida")
public record SimulacaoQuitacaoRequest(

        @Schema(example = "5000.00", description = "Valor total da dívida em R$")
        @NotNull(message = "O valor da dívida é obrigatório.")
        @Positive(message = "O valor da dívida deve ser positivo.")
        BigDecimal valorDivida,

        @Schema(example = "500.00", description = "Valor mensal hipotético disponível para pagamento em R$")
        @NotNull(message = "O valor mensal é obrigatório.")
        @Positive(message = "O valor mensal deve ser positivo.")
        BigDecimal valorMensal,

        @Schema(example = "0.02", description = "Taxa de juros mensal, em decimal (ex: 0.02 = 2% ao mês). Opcional — se omitido, calcula sem juros.")
        @DecimalMin(value = "0.0", message = "A taxa de juros não pode ser negativa.")
        BigDecimal taxaJurosMensal

) {}