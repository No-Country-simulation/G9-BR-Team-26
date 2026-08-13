package com.hackathon.one.dto.externo;

// Resposta recebida da API Python (Data Science) — endpoint /analise-financeira.
public record DataScienceAnaliseResponse(
        String perfil,
        Double probabilidade
) {}