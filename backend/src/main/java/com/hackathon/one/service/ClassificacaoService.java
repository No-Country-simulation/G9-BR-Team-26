package com.hackathon.one.service;

import com.hackathon.one.dto.ClassificacaoResponse;
import com.hackathon.one.dto.TransacaoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Classificação de transações por categoria.
 * ATENÇÃO: lógica MOCKADA por palavras-chave, enquanto o modelo real
 * do time de Data Science não é integrado.
 * Categorias alinhadas ao contrato oficial do repositório (README.md
 * principal e data-science/README.md): alimentacao, transporte, moradia,
 * saude, educacao, lazer, servicos, outros.
 */
@Service
@Slf4j
public class ClassificacaoService {

    public ClassificacaoResponse classificar(TransacaoRequest request) {
        String categoria = classificarPorPalavraChave(request.descricao());
        Double confianca = 0.85;
        log.info("Transação classificada | descrição: {} | categoria: {}", request.descricao(), categoria);
        return new ClassificacaoResponse(categoria, confianca);
    }

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