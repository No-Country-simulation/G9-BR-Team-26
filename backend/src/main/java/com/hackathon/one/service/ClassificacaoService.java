package com.hackathon.one.service;

import com.hackathon.one.dto.ClassificacaoResponse;
import com.hackathon.one.dto.TransacaoRequest;
import com.hackathon.one.dto.externo.DataScienceClassificarResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

/**
 * Classificação de transações por categoria.
 * Integração REAL com a API Python (Data Science), confirmada via Swagger
 * ao vivo em 11/08/2026. Se a API estiver indisponível, cai no fallback
 * mockado por palavras-chave, para não derrubar a experiência do usuário.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClassificacaoService {

    private final DataScienceApiClient dataScienceApiClient;

    public ClassificacaoResponse classificar(TransacaoRequest request) {
        try {
            DataScienceClassificarResponse resposta = dataScienceApiClient.classificar(
                    request.descricao(), request.valor().doubleValue()
            );
            log.info("Transação classificada (API real) | descrição: {} | categoria: {} | confiança: {}",
                    request.descricao(), resposta.categoria(), resposta.confianca());
            return new ClassificacaoResponse(resposta.categoria(), resposta.confianca());

        } catch (RestClientException e) {
            log.warn("API Python indisponível, usando fallback mockado. Erro: {}", e.getMessage());
            String categoria = classificarPorPalavraChave(request.descricao());
            return new ClassificacaoResponse(categoria, 0.5); // confiança baixa, sinaliza que é fallback
        }
    }

    // Fallback: só usado se a API Python estiver fora do ar.
    private String classificarPorPalavraChave(String descricao) {
        String texto = descricao.toLowerCase();

        if (texto.contains("supermercado") || texto.contains("mercado") || texto.contains("restaurante")
                || texto.contains("ifood") || texto.contains("burger")) {
            return "alimentacao";
        }
        if (texto.contains("combustivel") || texto.contains("uber") || texto.contains("posto")
                || texto.contains("trip")) {
            return "transporte";
        }
        if (texto.contains("streaming") || texto.contains("cinema") || texto.contains("netflix")
                || texto.contains("lazer")) {
            return "lazer";
        }
        if (texto.contains("farmacia") || texto.contains("hospital") || texto.contains("consulta")) {
            return "saude";
        }
        if (texto.contains("aluguel") || texto.contains("condominio") || texto.contains("energia")) {
            return "moradia";
        }
        if (texto.contains("escola") || texto.contains("curso") || texto.contains("faculdade")
                || texto.contains("livro")) {
            return "educacao";
        }
        if (texto.contains("assinatura") || texto.contains("mensalidade") || texto.contains("plano")) {
            return "servicos";
        }
        return "outros";
    }
}