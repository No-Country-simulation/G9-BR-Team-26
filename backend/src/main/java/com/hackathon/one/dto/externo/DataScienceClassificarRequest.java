package com.hackathon.one.dto.externo;

// Payload enviado para a API Python (Data Science) — endpoint /classificar.
public record DataScienceClassificarRequest(
        String descricao,
        Double valor
) {}