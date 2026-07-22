package com.hackathon.one.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

// Dados retornados ao cliente após uma análise financeira.
// "resumo_gastos" é calculado dinamicamente a partir das transações — não é persistido.
@Schema(name = "AnaliseFinanceiraResponse", description = "Resultado de uma análise financeira")
public record AnaliseFinanceiraResponse(

        @JsonProperty("perfil_financeiro")
        @Schema(example = "Em observacao", description = "Classificação do perfil financeiro")
        String perfilFinanceiro,

        @Schema(example = "0.82", description = "Probabilidade/confiança da classificação")
        Double probabilidade,

        @JsonProperty("resumo_gastos")
        @Schema(description = "Soma de gastos por categoria")
        Map<String, java.math.BigDecimal> resumoGastos,

        @Schema(description = "Lista de recomendações personalizadas")
        List<String> recomendacoes

) {}