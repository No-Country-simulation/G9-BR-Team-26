package com.hackathon.one.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

@Schema(name = "MetaFinanceiraRequest", description = "Payload para criar uma meta financeira")
public record MetaFinanceiraRequest(

        @Schema(example = "Reduzir endividamento para 15%")
        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @Schema(example = "15", description = "Nível de endividamento desejado (0 a 100)")
        @NotNull(message = "O endividamento alvo é obrigatório.")
        @PositiveOrZero(message = "O endividamento alvo não pode ser negativo.")
        Integer endividamentoAlvo,

        @Schema(example = "2026-12-31", description = "Data limite para atingir a meta")
        @NotNull(message = "A data alvo é obrigatória.")
        @Future(message = "A data alvo deve ser no futuro.")
        LocalDate dataAlvo

) {}