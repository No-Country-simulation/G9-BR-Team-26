package com.hackathon.one.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// Resposta da classificação automática de uma transação.
@Schema(name = "ClassificacaoResponse", description = "Resultado da classificação de uma transação")
public record ClassificacaoResponse(

        @Schema(example = "alimentacao", description = "Categoria classificada da transação")
        String categoria

) {}