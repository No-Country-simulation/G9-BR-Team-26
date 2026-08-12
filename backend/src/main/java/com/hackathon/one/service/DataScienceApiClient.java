package com.hackathon.one.service;

import com.hackathon.one.dto.externo.DataScienceAnaliseRequest;
import com.hackathon.one.dto.externo.DataScienceAnaliseResponse;
import com.hackathon.one.dto.externo.DataScienceClassificarRequest;
import com.hackathon.one.dto.externo.DataScienceClassificarResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente HTTP para a API Python (Data Science / FastAPI).
 * Contrato confirmado via Swagger ao vivo da API (http://localhost:8000/docs)
 * em 11/08/2026 — não depende de documentação escrita, que estava desatualizada.
 */
@Component
@Slf4j
public class DataScienceApiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String token;

    public DataScienceApiClient(
            RestTemplate dataScienceRestTemplate,
            @Value("${datascience.api.url}") String baseUrl,
            @Value("${datascience.api.token}") String token
    ) {
        this.restTemplate = dataScienceRestTemplate;
        this.baseUrl = baseUrl;
        this.token = token;
    }

    public DataScienceClassificarResponse classificar(String descricao, Double valor) {
        DataScienceClassificarRequest request = new DataScienceClassificarRequest(descricao, valor);
        HttpEntity<DataScienceClassificarRequest> entity = new HttpEntity<>(request, buildHeaders());

        try {
            return restTemplate.postForObject(
                    baseUrl + "/classificar",
                    entity,
                    DataScienceClassificarResponse.class
            );
        } catch (RestClientException e) {
            log.error("Erro ao chamar /classificar na API Python: {}", e.getMessage());
            throw e;
        }
    }

    public DataScienceAnaliseResponse analisar(
            java.math.BigDecimal rendaMensal, Integer nivelEndividamento, String frequenciaPoupanca
    ) {
        DataScienceAnaliseRequest request = new DataScienceAnaliseRequest(
                rendaMensal, nivelEndividamento, frequenciaPoupanca
        );
        HttpEntity<DataScienceAnaliseRequest> entity = new HttpEntity<>(request, buildHeaders());

        try {
            return restTemplate.postForObject(
                    baseUrl + "/analise-financeira",
                    entity,
                    DataScienceAnaliseResponse.class
            );
        } catch (RestClientException e) {
            log.error("Erro ao chamar /analise-financeira na API Python: {}", e.getMessage());
            throw e;
        }
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }
}