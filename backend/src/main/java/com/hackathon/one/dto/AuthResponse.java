package com.hackathon.one.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "AuthResponse", description = "Resposta após autenticação com token JWT")
public class AuthResponse {
    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", description = "Token JWT para autenticação")
    @JsonProperty("accessToken")
    @JsonAlias({"access_token", "AccessToken"})
    private String accessToken;

    @Schema(example = "Bearer", description = "Tipo do token")
    @JsonProperty("tokenType")
    @JsonAlias({"token_type", "tokenType"})
    private String tokenType;

    @Schema(example = "3600", description = "Tempo de expiração do token em segundos")
    @JsonProperty("expiresIn")
    @JsonAlias({"expires_in", "expiresIn"})
    private Long expiresIn;
}
