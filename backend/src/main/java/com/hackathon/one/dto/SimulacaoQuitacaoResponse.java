package com.hackathon.one.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "SimulacaoQuitacaoResponse", description = "Resultado da simulação de quitação de dívida")
public record SimulacaoQuitacaoResponse(

        @Schema(example = "5000.00") BigDecimal valorDivida,
        @Schema(example = "500.00") BigDecimal valorMensal,

        @Schema(example = "0.02", description = "Null se a simulação foi feita sem juros")
        BigDecimal taxaJurosMensal,

        @Schema(example = "10", description = "Número de meses necessários para quitar a dívida")
        Integer meses,

        @Schema(example = "5000.00", description = "Valor total pago ao final (meses x valor mensal)")
        BigDecimal valorTotalPago,

        @Schema(example = "0.00", description = "Diferença entre o total pago e a dívida original (juros pagos). Zero se sem juros.")
        BigDecimal totalJurosPagos

) {}