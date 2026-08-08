package com.hackathon.one.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ClassificacaoResponse", description = "Resultado da classificação de uma transação")
public record ClassificacaoResponse(

        @Schema(example = "alimentacao", description = "Categoria classificada da transação")
        String categoria,

        @Schema(example = "0.94", description = "Confiança do modelo na classificação (0 a 1). No mock atual, valor fixo até a integração real.")
        Double confianca

) {}