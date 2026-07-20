package com.hackathon.one.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(name = "UserResponse", description = "Dados públicos do usuário")
public class UserResponse {
    @Schema(example = "1", description = "Identificador numérico do usuário")
    private Long id;
    @Schema(example = "Maria Silva", description = "Nome do usuário")
    private String nome;
    @Schema(example = "maria.silva@email.com", description = "E-mail do usuário")
    private String email;
    @Schema(example = "2026-07-20T14:30:00", description = "Data de criação da conta")
    @JsonProperty("criado_em")
    private LocalDateTime criadoEm;
}
