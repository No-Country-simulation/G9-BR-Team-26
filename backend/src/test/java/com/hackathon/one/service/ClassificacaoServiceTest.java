package com.hackathon.one.service;

import com.hackathon.one.dto.ClassificacaoResponse;
import com.hackathon.one.dto.TransacaoRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ClassificacaoServiceTest {

    private final ClassificacaoService classificacaoService = new ClassificacaoService();

    @Test
    void shouldClassifyFoodRelatedTransactionAsAlimentacao() {
        TransacaoRequest request = new TransacaoRequest("Supermercado Extra", new BigDecimal("89.90"));

        ClassificacaoResponse response = classificacaoService.classificar(request);

        assertThat(response.categoria()).isEqualTo("alimentacao");
    }

    @Test
    void shouldClassifyTransportTransactionAsTransporte() {
        TransacaoRequest request = new TransacaoRequest("Uber para o trabalho", new BigDecimal("15.50"));

        ClassificacaoResponse response = classificacaoService.classificar(request);

        assertThat(response.categoria()).isEqualTo("transporte");
    }

    @Test
    void shouldFallbackToOutrosWhenNoKnownKeywordMatches() {
        TransacaoRequest request = new TransacaoRequest("Pagamento pontual", new BigDecimal("5.00"));

        ClassificacaoResponse response = classificacaoService.classificar(request);

        assertThat(response.categoria()).isEqualTo("outros");
    }
}
