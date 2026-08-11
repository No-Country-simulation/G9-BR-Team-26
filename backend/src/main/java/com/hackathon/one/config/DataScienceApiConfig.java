package com.hackathon.one.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuração do cliente HTTP usado para chamar a API Python (Data Science).
 * Timeout explícito: sem isso, uma instabilidade na FastAPI faria o Spring
 * esperar indefinidamente, travando a requisição do usuário final.
 */
@Configuration
public class DataScienceApiConfig {

    @Value("${datascience.api.connect-timeout-ms}")
    private int connectTimeoutMs;

    @Value("${datascience.api.read-timeout-ms}")
    private int readTimeoutMs;

    @Bean
    public RestTemplate dataScienceRestTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(connectTimeoutMs))
                .setReadTimeout(Duration.ofMillis(readTimeoutMs))
                .build();
    }
}