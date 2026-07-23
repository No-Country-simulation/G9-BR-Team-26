package com.hackathon.one.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Representa uma análise financeira já salva, para consulta de histórico.
// NOTA: não inclui "resumo_gastos", pois esse campo é calculado dinamicamente
// a partir das transações no momento da análise e não é persistido no banco.
@Schema(name = "AnaliseFinanceiraHistoricoResponse", description = "Análise financeira salva, para consulta de histórico")
public record AnaliseFinanceiraHistoricoResponse(

        @Schema(example = "1") Long id,

        @JsonProperty("criado_em")
        @Schema(example = "2026-07-21T17:44:00") LocalDateTime criadoEm,

        @JsonProperty("renda_mensal")
        @Schema(example = "4500") BigDecimal rendaMensal,

        @JsonProperty("nivel_endividamento")
        @Schema(example = "25") Integer nivelEndividamento,

        @JsonProperty("frequencia_poupanca")
        @Schema(example = "Media") String frequenciaPoupanca,

        @JsonProperty("perfil_financeiro")
        @Schema(example = "Em observacao") String perfilFinanceiro,

        @Schema(example = "0.75") Double probabilidade,

        @Schema(description = "Recomendações geradas nessa análise")
        List<String> recomendacoes

) {}