package com.hackathon.one.dto.externo;

// Resposta recebida da API Python (Data Science) — endpoint /classificar.
public record DataScienceClassificarResponse(
        String categoria,
        Double confianca
) {}