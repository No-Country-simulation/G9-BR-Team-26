package com.hackathon.one.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthResponseTest {

    @Test
    void shouldSerializeAccessTokenWithCamelCaseFieldName() throws JsonProcessingException {
        AuthResponse response = AuthResponse.builder()
                .accessToken("jwt-token")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .build();

        String json = new ObjectMapper().writeValueAsString(response);

        assertThat(json).contains("\"accessToken\"").doesNotContain("\"access_token\"");
    }
}
