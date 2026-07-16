package com.hackathon.one.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String id;
    private String nome;
    private String email;
    @JsonProperty("criado_em")
    private LocalDateTime criadoEm;
}
