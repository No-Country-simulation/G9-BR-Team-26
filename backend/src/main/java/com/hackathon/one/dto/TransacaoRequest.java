package com.hackathon.one.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

// Dados enviados pelo cliente para criar uma nova transação.
// O usuário dono da transação é identificado pelo token JWT — nunca pelo body.
// A categoria não é enviada pelo cliente neste momento; ela será classificada posteriormente pelo modelo de ML.
@Schema(name = "TransacaoRequest", description = "Payload para cadastro de uma transação")
public record TransacaoRequest(

        @Schema(example = "Supermercado", description = "Descrição da transação")
        @NotBlank(message = "A descrição não pode estar em branco.")
        String descricao,

        @Schema(example = "89.90", description = "Valor da transação")
        @NotNull(message = "O valor é obrigatório.")
        @Positive(message = "O valor deve ser positivo.")
        BigDecimal valor
) {}
