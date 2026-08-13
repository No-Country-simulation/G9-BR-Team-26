package com.hackathon.one.dto.externo;

// Payload enviado para a API Python (Data Science) — endpoint /analise-financeira.
public record DataScienceAnaliseRequest(
        java.math.BigDecimal rendaMensal,
        Integer nivelEndividamento,
        String frequenciaPoupanca
) {}