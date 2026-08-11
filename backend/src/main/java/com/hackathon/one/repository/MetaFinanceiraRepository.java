package com.hackathon.one.repository;

import com.hackathon.one.domain.MetaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaFinanceiraRepository extends JpaRepository<MetaFinanceira, Long> {

    List<MetaFinanceira> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
}