package com.hackathon.one.service;

import com.hackathon.one.domain.Transacao;
import com.hackathon.one.domain.Usuario;
import com.hackathon.one.dto.TransacaoRequest;
import com.hackathon.one.dto.TransacaoResponse;
import com.hackathon.one.repository.TransacaoRepository;
import com.hackathon.one.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @Test
    void shouldPersistCategoryAsOutrosRegardlessOfIncomingValue() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .email("usuario@email.com")
                .build();

        TransacaoRequest request = new TransacaoRequest(
                "Supermercado",
                new BigDecimal("89.90")
        );

        when(usuarioRepository.findByEmail("usuario@email.com")).thenReturn(Optional.of(usuario));
        when(transacaoRepository.save(any(Transacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transacaoService.criar(request, "usuario@email.com");

        ArgumentCaptor<Transacao> captor = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).save(captor.capture());

        assertThat(captor.getValue().getCategoria()).isEqualTo("outros");
    }

    @Test
    void shouldUpdateTransactionForOwner() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .email("usuario@email.com")
                .build();

        Transacao transacao = Transacao.builder()
                .id(10L)
                .descricao("Antiga")
                .valor(new BigDecimal("50.00"))
                .categoria("outros")
                .usuario(usuario)
                .build();

        TransacaoRequest request = new TransacaoRequest("Nova", new BigDecimal("75.50"));

        when(usuarioRepository.findByEmail("usuario@email.com")).thenReturn(Optional.of(usuario));
        when(transacaoRepository.findById(10L)).thenReturn(Optional.of(transacao));
        when(transacaoRepository.save(any(Transacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransacaoResponse response = transacaoService.atualizar(10L, request, "usuario@email.com");

        assertThat(response.descricao()).isEqualTo("Nova");
        assertThat(response.valor()).isEqualTo(new BigDecimal("75.50"));
        assertThat(response.categoria()).isEqualTo("outros");
    }

    @Test
    void shouldDeleteTransactionForOwner() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .email("usuario@email.com")
                .build();

        Transacao transacao = Transacao.builder()
                .id(10L)
                .descricao("Para apagar")
                .valor(new BigDecimal("20.00"))
                .categoria("outros")
                .usuario(usuario)
                .build();

        when(usuarioRepository.findByEmail("usuario@email.com")).thenReturn(Optional.of(usuario));
        when(transacaoRepository.findById(10L)).thenReturn(Optional.of(transacao));
        doNothing().when(transacaoRepository).delete(transacao);

        transacaoService.deletar(10L, "usuario@email.com");

        verify(transacaoRepository).delete(transacao);
    }
}
