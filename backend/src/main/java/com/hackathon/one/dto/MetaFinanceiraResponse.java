package com.hackathon.one.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "MetaFinanceiraResponse", description = "Meta financeira com progresso calculado")
public record MetaFinanceiraResponse(

        @Schema(example = "1") Long id,
        @Schema(example = "Reduzir endividamento para 15%") String descricao,

        @JsonProperty("endividamento_alvo")
        @Schema(example = "15") Integer endividamentoAlvo,

        @JsonProperty("endividamento_atual")
        @Schema(example = "25", description = "Nível de endividamento da análise mais recente. Null se o usuário nunca fez uma análise.")
        Integer endividamentoAtual,

        @JsonProperty("data_alvo")
        @Schema(example = "2026-12-31") LocalDate dataAlvo,

        @JsonProperty("criado_em")
        @Schema(example = "2026-08-08T10:00:00") LocalDateTime criadoEm,

        @Schema(example = "false") Boolean concluida,

        @JsonProperty("progresso_percentual")
        @Schema(example = "40.0", description = "Percentual de progresso em direção à meta (0 a 100). Null se não houver análise ainda.")
        Double progressoPercentual

) {}