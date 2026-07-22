package com.hackathon.one.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

// Formato padrão de erro devolvido por toda a API.
// Usado tanto para erros simples (mensagem única) quanto para
// erros de validação (múltiplos campos inválidos).
@Schema(name = "ErrorResponse", description = "Formato padrão de erro da API")
public record ErrorResponse(

        @Schema(example = "2026-07-21T17:00:00") LocalDateTime timestamp,
        @Schema(example = "404") int status,
        @Schema(example = "Not Found") String error,
        @Schema(example = "Usuario com id 42 não encontrado.") String message,
        @Schema(example = "/usuarios/42") String path,
        @Schema(description = "Detalhes de erros de validação por campo, quando aplicável")
        List<String> detalhes

) {
        // Construtor de conveniência para erros simples, sem lista de detalhes.
        public static ErrorResponse of(int status, String error, String message, String path) {
                return new ErrorResponse(LocalDateTime.now(), status, error, message, path, null);
        }

        // Construtor de conveniência para erros de validação, com lista de campos.
        public static ErrorResponse ofValidation(int status, String error, String message, String path, List<String> detalhes) {
                return new ErrorResponse(LocalDateTime.now(), status, error, message, path, detalhes);
        }
}