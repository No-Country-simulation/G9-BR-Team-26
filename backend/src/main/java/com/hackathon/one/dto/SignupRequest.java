package com.hackathon.one.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "SignupRequest", description = "Payload para cadastro de novo usuário")
public class SignupRequest {
    @Schema(example = "Maria Silva", description = "Nome completo do usuário")
    @NotBlank
    private String nome;

    @Schema(example = "maria.silva@email.com", description = "E-mail único para login")
    @NotBlank
    @Email
    private String email;

    @Schema(example = "123456", description = "Senha para acesso")
    @NotBlank
    private String senha;
}
