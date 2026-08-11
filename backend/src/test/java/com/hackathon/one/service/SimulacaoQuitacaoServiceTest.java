package com.hackathon.one.service;

import com.hackathon.one.dto.SimulacaoQuitacaoRequest;
import com.hackathon.one.dto.SimulacaoQuitacaoResponse;
import com.hackathon.one.exception.ArquivoInvalidoException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimulacaoQuitacaoServiceTest {

    private final SimulacaoQuitacaoService simulacaoQuitacaoService = new SimulacaoQuitacaoService();

    @Test
    void shouldCalculateMonthsWithoutInterest() {
        SimulacaoQuitacaoRequest request = new SimulacaoQuitacaoRequest(
                new BigDecimal("5000"), new BigDecimal("500"), null
        );

        SimulacaoQuitacaoResponse response = simulacaoQuitacaoService.simular(request);

        assertThat(response.meses()).isEqualTo(10);
        assertThat(response.valorTotalPago()).isEqualByComparingTo("5000");
        assertThat(response.totalJurosPagos()).isEqualByComparingTo("0");
    }

    @Test
    void shouldCalculateMoreMonthsWhenInterestIsApplied() {
        SimulacaoQuitacaoRequest request = new SimulacaoQuitacaoRequest(
                new BigDecimal("5000"), new BigDecimal("500"), new BigDecimal("0.02")
        );

        SimulacaoQuitacaoResponse response = simulacaoQuitacaoService.simular(request);

        assertThat(response.meses()).isGreaterThan(10);
        assertThat(response.totalJurosPagos()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void shouldThrowExceptionWhenMonthlyValueDoesNotCoverInterest() {
        SimulacaoQuitacaoRequest request = new SimulacaoQuitacaoRequest(
                new BigDecimal("5000"), new BigDecimal("50"), new BigDecimal("0.05")
        );

        assertThatThrownBy(() -> simulacaoQuitacaoService.simular(request))
                .isInstanceOf(ArquivoInvalidoException.class)
                .hasMessageContaining("não é suficiente");
    }
}