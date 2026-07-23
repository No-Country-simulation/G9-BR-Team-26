package com.hackathon.one.service;

import com.hackathon.one.dto.ClassificacaoResponse;
import com.hackathon.one.dto.TransacaoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Classificação de transações por categoria.
 * ATENÇÃO: lógica MOCKADA por palavras-chave, enquanto o modelo real
 * do time de Data Science (classificador de texto) não é integrado
 * (ver card "Integração real com API Python" — bloqueado até o Bloco 5 de DS).
 */
@Service
@Slf4j
public class ClassificacaoService {

    public ClassificacaoResponse classificar(TransacaoRequest request) {
        String categoria = classificarPorPalavraChave(request.descricao());
        log.info("Transação classificada | descrição: {} | categoria: {}", request.descricao(), categoria);
        return new ClassificacaoResponse(categoria);
    }

    // Mock simples: procura palavras-chave na descrição, sem acentuação,
    // e devolve a categoria correspondente. Se nada bater, cai em "outros".
    private String classificarPorPalavraChave(String descricao) {
        String texto = descricao.toLowerCase();

        if (texto.contains("supermercado") || texto.contains("mercado") || texto.contains("restaurante")) {
            return "alimentacao";
        }
        if (texto.contains("combustivel") || texto.contains("uber") || texto.contains("posto")) {
            return "transporte";
        }
        if (texto.contains("streaming") || texto.contains("cinema") || texto.contains("netflix")) {
            return "entretenimento";
        }
        if (texto.contains("farmacia") || texto.contains("hospital") || texto.contains("consulta")) {
            return "saude";
        }
        if (texto.contains("aluguel") || texto.contains("condominio") || texto.contains("energia")) {
            return "moradia";
        }
        return "outros";
    }
}