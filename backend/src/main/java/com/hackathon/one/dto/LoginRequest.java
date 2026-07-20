package com.hackathon.one.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "LoginRequest", description = "Payload para autenticação do usuário")
public class LoginRequest {
    @Schema(example = "usuario@email.com", description = "E-mail cadastrado")
    @NotBlank
    @Email
    private String email;

    @Schema(example = "123456", description = "Senha cadastrada")
    @NotBlank
    private String senha;
}
