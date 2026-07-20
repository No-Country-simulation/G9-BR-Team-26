package com.hackathon.one.repository;

import com.hackathon.one.domain.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repositório de acesso a dados para {@link Transacao}.
// Fornece os métodos CRUD padrão do Spring Data JPA,
// mais a consulta por usuário.
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    // Retorna todas as transações de um determinado usuário,
    // ordenadas da mais recente para a mais antiga.
    List<Transacao> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
}
