package com.hackathon.one.service;

import com.hackathon.one.domain.Usuario;
import com.hackathon.one.dto.AnaliseFinanceiraRequest;
import com.hackathon.one.dto.AnaliseFinanceiraResponse;
import com.hackathon.one.repository.AnaliseFinanceiraRepository;
import com.hackathon.one.repository.TransacaoRepository;
import com.hackathon.one.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnaliseFinanceiraServiceTest {

    @Mock
    private AnaliseFinanceiraRepository analiseFinanceiraRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AnaliseFinanceiraService analiseFinanceiraService;

    private Usuario usuarioMock() {
        return Usuario.builder().id(1L).nome("Teste").email("teste@email.com").build();
    }

    @Test
    void shouldCalculateHighScoreForLowDebtAndHighSavings() {
        Usuario usuario = usuarioMock();
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(transacaoRepository.findByUsuarioIdOrderByCriadoEmDesc(1L)).thenReturn(Collections.emptyList());

        AnaliseFinanceiraRequest request = new AnaliseFinanceiraRequest(
                new java.math.BigDecimal("4500"), 10, "Alta"
        );

        AnaliseFinanceiraResponse response = analiseFinanceiraService.analisar(request, "teste@email.com");

        // score = 100 - 10 + 10 (poupança Alta) = 100 (limitado ao máximo)
        assertThat(response.score()).isEqualTo(100);
    }

    @Test
    void shouldCalculateLowScoreForHighDebtAndLowSavings() {
        Usuario usuario = usuarioMock();
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(transacaoRepository.findByUsuarioIdOrderByCriadoEmDesc(1L)).thenReturn(Collections.emptyList());

        AnaliseFinanceiraRequest request = new AnaliseFinanceiraRequest(
                new java.math.BigDecimal("4500"), 95, "Baixa"
        );

        AnaliseFinanceiraResponse response = analiseFinanceiraService.analisar(request, "teste@email.com");

        // score = 100 - 95 - 10 (poupança Baixa) = -5, limitado a 0
        assertThat(response.score()).isEqualTo(0);
    }

    @Test
    void shouldApplyNoAdjustmentForMediumSavings() {
        Usuario usuario = usuarioMock();
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(transacaoRepository.findByUsuarioIdOrderByCriadoEmDesc(1L)).thenReturn(Collections.emptyList());

        AnaliseFinanceiraRequest request = new AnaliseFinanceiraRequest(
                new java.math.BigDecimal("4500"), 25, "Media"
        );

        AnaliseFinanceiraResponse response = analiseFinanceiraService.analisar(request, "teste@email.com");

        // score = 100 - 25 + 0 = 75
        assertThat(response.score()).isEqualTo(75);
    }
}