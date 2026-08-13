package com.hackathon.one.service;

import com.hackathon.one.dto.ClassificacaoResponse;
import com.hackathon.one.dto.TransacaoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Testa o FALLBACK mockado do ClassificacaoService — usado quando a API
 * Python (Data Science) está indisponível. A classificação via API real
 * não é testada aqui (depende de rede/API externa); é validada manualmente
 * via smoke test com a API rodando.
 */
@ExtendWith(MockitoExtension.class)
class ClassificacaoServiceTest {

    @Mock
    private DataScienceApiClient dataScienceApiClient;

    @InjectMocks
    private ClassificacaoService classificacaoService;

    @Test
    void shouldFallbackToAlimentacaoForFoodRelatedTransaction() {
        when(dataScienceApiClient.classificar("Supermercado Extra", 89.90))
                .thenThrow(new RestClientException("API indisponível (simulado)"));

        TransacaoRequest request = new TransacaoRequest("Supermercado Extra", new BigDecimal("89.90"));
        ClassificacaoResponse response = classificacaoService.classificar(request);

        assertThat(response.categoria()).isEqualTo("alimentacao");
    }

    @Test
    void shouldFallbackToTransporteForTransportRelatedTransaction() {
        when(dataScienceApiClient.classificar("Uber para o trabalho", 15.50))
                .thenThrow(new RestClientException("API indisponível (simulado)"));

        TransacaoRequest request = new TransacaoRequest("Uber para o trabalho", new BigDecimal("15.50"));
        ClassificacaoResponse response = classificacaoService.classificar(request);

        assertThat(response.categoria()).isEqualTo("transporte");
    }

    @Test
    void shouldFallbackToOutrosWhenNoKnownKeywordMatches() {
        when(dataScienceApiClient.classificar("Pagamento pontual", 5.00))
                .thenThrow(new RestClientException("API indisponível (simulado)"));

        TransacaoRequest request = new TransacaoRequest("Pagamento pontual", new BigDecimal("5.00"));
        ClassificacaoResponse response = classificacaoService.classificar(request);

        assertThat(response.categoria()).isEqualTo("outros");
    }
}