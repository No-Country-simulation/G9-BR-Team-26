package com.hackathon.one.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class GeminiApiConfig {

    @Bean
    public RestTemplate geminiRestTemplate(
            RestTemplateBuilder builder,
            @Value("${fai.gemini.connect-timeout-ms}") int connectTimeoutMs,
            @Value("${fai.gemini.read-timeout-ms}") int readTimeoutMs
    ) {
        return builder
                .setConnectTimeout(Duration.ofMillis(connectTimeoutMs))
                .setReadTimeout(Duration.ofMillis(readTimeoutMs))
                .build();
    }
}
