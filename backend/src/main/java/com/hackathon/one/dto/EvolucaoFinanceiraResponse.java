package com.hackathon.one.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

// Compara a análise financeira mais recente do usuário com a anterior,
// mostrando a evolução do perfil, score e nível de endividamento ao longo do tempo.
@Schema(name = "EvolucaoFinanceiraResponse", description = "Comparação entre a análise mais recente e a anterior")
public record EvolucaoFinanceiraResponse(

        @JsonProperty("perfil_atual")
        @Schema(example = "Em observacao") String perfilAtual,

        @JsonProperty("perfil_anterior")
        @Schema(example = "Em risco", description = "Null se não houver análise anterior")
        String perfilAnterior,

        @JsonProperty("score_atual")
        @Schema(example = "85", description = "Null se não houver análise anterior")
        Integer scoreAtual,

        @JsonProperty("score_anterior")
        @Schema(example = "70", description = "Null se não houver análise anterior")
        Integer scoreAnterior,

        @JsonProperty("data_analise_atual")
        @Schema(example = "2026-08-08T15:00:00") LocalDateTime dataAnaliseAtual,

        @JsonProperty("data_analise_anterior")
        @Schema(example = "2026-08-01T10:00:00", description = "Null se não houver análise anterior")
        LocalDateTime dataAnaliseAnterior,

        @JsonProperty("variacao_endividamento")
        @Schema(example = "-5", description = "Diferença no nível de endividamento (negativo = melhora). Null se não houver análise anterior")
        Integer variacaoEndividamento,

        @JsonProperty("tendencia")
        @Schema(example = "melhora", description = "melhora, piora, estavel ou indisponivel (sem histórico suficiente)")
        String tendencia

) {}