package com.hackathon.one.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

// Resumo do processamento de um arquivo CSV de transações em lote.
@Schema(name = "ProcessamentoLoteResponse", description = "Resumo do processamento de transações em lote via CSV")
public record ProcessamentoLoteResponse(

        @Schema(example = "8", description = "Total de linhas processadas com sucesso")
        int totalCriadas,

        @Schema(example = "2", description = "Total de linhas que falharam")
        int totalFalhas,

        @Schema(description = "Detalhes das linhas que falharam, com o motivo de cada erro")
        List<String> erros

) {}