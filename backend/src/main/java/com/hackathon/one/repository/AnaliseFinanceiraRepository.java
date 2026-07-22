package com.hackathon.one.repository;

import com.hackathon.one.domain.AnaliseFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repositório de acesso a dados para {@link AnaliseFinanceira}.
public interface AnaliseFinanceiraRepository extends JpaRepository<AnaliseFinanceira, Long> {

    // Retorna todas as análises de um usuário, da mais recente para a mais antiga.
    // Mesmo padrão de nome usado em TransacaoRepository (findByUsuarioIdOrderByCriadoEmDesc).
    List<AnaliseFinanceira> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
}