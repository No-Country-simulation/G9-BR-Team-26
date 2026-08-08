package com.hackathon.one.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Transforma o resumo de gastos (Map por categoria) no formato de campos
 * nomeados "gasto_*" esperado pela API Python (ver README.md do repositório).
 * Categorias oficiais: alimentacao, transporte, lazer, moradia, saude,
 * educacao, servicos, outros.
 */
@Component
public class GastoPayloadMapper {

    private static final String[] CATEGORIAS = {
            "alimentacao", "transporte", "lazer", "moradia",
            "saude", "educacao", "servicos", "outros"
    };

    public Map<String, BigDecimal> paraPayloadGastoPorCategoria(Map<String, BigDecimal> resumoGastos) {
        Map<String, BigDecimal> payload = new LinkedHashMap<>();
        for (String categoria : CATEGORIAS) {
            BigDecimal valor = resumoGastos.getOrDefault(categoria, BigDecimal.ZERO);
            payload.put("gasto_" + categoria, valor);
        }
        return payload;
    }
}