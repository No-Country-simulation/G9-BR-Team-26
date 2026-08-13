package com.hackathon.one.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FaiChatRequest(
        @NotBlank(message = "A mensagem é obrigatória.")
        @Size(max = 2000, message = "A mensagem deve ter no máximo 2000 caracteres.")
        String mensagem
) {
}
