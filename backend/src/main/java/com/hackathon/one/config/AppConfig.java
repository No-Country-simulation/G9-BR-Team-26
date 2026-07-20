package com.hackathon.one.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração centralizada de beans de infraestrutura da aplicação.
 */
@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
              .setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    @Bean
    public OpenAPI financeOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization")
                .description("Informe o token JWT no formato: Bearer <accessToken>");

        return new OpenAPI()
                .info(new Info()
                        .title("Finance AI API")
                        .version("1.0.0")
                        .description("API para autenticação, gestão de transações e análise financeira com suporte a IA."))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .tags(List.of(
                        new Tag().name("Autenticação").description("Cadastro e login do usuário"),
                        new Tag().name("Transações").description("Gestão de transações financeiras"),
                        new Tag().name("Usuários").description("Consulta de perfil e dados do usuário")
                ));
    }
}
