package com.hackathon.one.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.one.dto.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Limita o número de requisições por usuário (ou IP, se não autenticado)
 * dentro de uma janela de tempo fixa. Implementação simples em memória,
 * sem dependência externa — suficiente para o escopo do MVP.
 * ATENÇÃO: como é em memória, o contador reseta se a aplicação reiniciar,
 * e não é compartilhado entre múltiplas instâncias (não é um problema
 * para este projeto, que roda uma única instância).
 */
@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    // Ajuste este valor para testar mais rápido (ex: 5) e volte para 60 depois do teste.
    private static final int LIMITE_REQUISICOES = 60;
    private static final long JANELA_MS = 60_000; // 1 minuto

    private final ConcurrentHashMap<String, Janela> contadores = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String chave = resolverChave(request);
        Janela janela = contadores.computeIfAbsent(chave, k -> new Janela());

        boolean excedeu;
        synchronized (janela) {
            long agora = System.currentTimeMillis();
            // Se a janela de 1 minuto já passou, reseta o contador.
            if (agora - janela.inicio > JANELA_MS) {
                janela.inicio = agora;
                janela.contagem.set(0);
            }
            excedeu = janela.contagem.incrementAndGet() > LIMITE_REQUISICOES;
        }

        if (excedeu) {
            log.warn("Rate limit excedido para: {}", chave);
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            ErrorResponse error = ErrorResponse.of(
                    429,
                    "Too Many Requests",
                    "Limite de requisições excedido. Tente novamente em instantes.",
                    request.getRequestURI()
            );
            response.getWriter().write(objectMapper.writeValueAsString(error));
            return;
        }

        filterChain.doFilter(request, response);
    }

    // Usa o e-mail do usuário autenticado como chave, quando disponível.
    // Se ainda não houver autenticação (ex: rota pública), usa o IP.
    private String resolverChave(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return request.getRemoteAddr();
    }

    // Classe auxiliar interna: guarda o início da janela atual e a contagem.
    private static class Janela {
        volatile long inicio = System.currentTimeMillis();
        AtomicInteger contagem = new AtomicInteger(0);
    }
}