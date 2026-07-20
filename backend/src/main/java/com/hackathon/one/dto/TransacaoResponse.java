package com.hackathon.one.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Dados retornados ao cliente após criar ou consultar uma transação.
// Nunca expõe a entidade {@link com.hackathon.one.domain.Transacao} diretamente.
@Schema(name = "TransacaoResponse", description = "Resposta com os dados de uma transação cadastrada")
public record TransacaoResponse(

        @Schema(example = "1") Long id,

        @Schema(example = "Supermercado") String descricao,

        @Schema(example = "89.90") BigDecimal valor,

        @Schema(example = "alimentacao") String categoria,

        @JsonProperty("usuario_id")
        @Schema(example = "1") Long usuarioId,

        @JsonProperty("criado_em")
        @Schema(example = "2026-07-20T14:30:00") LocalDateTime criadoEm
) {}
