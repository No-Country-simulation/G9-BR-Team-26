package com.hackathon.one.service;

import com.hackathon.one.domain.AnaliseFinanceira;
import com.hackathon.one.domain.Usuario;
import com.hackathon.one.dto.MetaFinanceiraRequest;
import com.hackathon.one.dto.MetaFinanceiraResponse;
import com.hackathon.one.repository.AnaliseFinanceiraRepository;
import com.hackathon.one.repository.MetaFinanceiraRepository;
import com.hackathon.one.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetaFinanceiraServiceTest {

    @Mock
    private MetaFinanceiraRepository metaFinanceiraRepository;

    @Mock
    private AnaliseFinanceiraRepository analiseFinanceiraRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MetaFinanceiraService metaFinanceiraService;

    private Usuario usuarioMock() {
        return Usuario.builder().id(1L).nome("Teste").email("teste@email.com").build();
    }

    private AnaliseFinanceira analiseMock(Integer nivelEndividamento) {
        return AnaliseFinanceira.builder()
                .id(1L)
                .nivelEndividamento(nivelEndividamento)
                .build();
    }

    @Test
    void shouldReturnZeroProgressWhenGoalWasJustCreated() {
        Usuario usuario = usuarioMock();
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(analiseFinanceiraRepository.findByUsuarioIdOrderByCriadoEmDesc(1L))
                .thenReturn(List.of(analiseMock(20)));

        MetaFinanceiraRequest request = new MetaFinanceiraRequest(
                "Reduzir endividamento", 10, LocalDate.of(2026, 12, 31)
        );

        MetaFinanceiraResponse response = metaFinanceiraService.criar(request, "teste@email.com");

        // Endividamento inicial = atual = 20, então progresso deve ser 0.
        assertThat(response.progressoPercentual()).isEqualTo(0.0);
        assertThat(response.endividamentoAtual()).isEqualTo(20);
    }

    @Test
    void shouldReturnFullProgressWhenTargetIsAlreadyReached() {
        Usuario usuario = usuarioMock();
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(analiseFinanceiraRepository.findByUsuarioIdOrderByCriadoEmDesc(1L))
                .thenReturn(List.of(analiseMock(5)));

        MetaFinanceiraRequest request = new MetaFinanceiraRequest(
                "Reduzir endividamento", 10, LocalDate.of(2026, 12, 31)
        );

        MetaFinanceiraResponse response = metaFinanceiraService.criar(request, "teste@email.com");

        // Endividamento atual (5) já é menor que o alvo (10) — meta batida.
        assertThat(response.progressoPercentual()).isEqualTo(100.0);
    }

    @Test
    void shouldReturnNullProgressWhenUserHasNoAnalysisYet() {
        Usuario usuario = usuarioMock();
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(analiseFinanceiraRepository.findByUsuarioIdOrderByCriadoEmDesc(1L))
                .thenReturn(Collections.emptyList());

        MetaFinanceiraRequest request = new MetaFinanceiraRequest(
                "Reduzir endividamento", 10, LocalDate.of(2026, 12, 31)
        );

        MetaFinanceiraResponse response = metaFinanceiraService.criar(request, "teste@email.com");

        assertThat(response.progressoPercentual()).isNull();
        assertThat(response.endividamentoAtual()).isNull();
    }
}