package com.hackathon.one.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "transacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Descrição legível da transação (ex: "Supermercado Extra")
    @Column(nullable = false)
    private String descricao;

    // Valor monetário da transação. Sempre positivo. 
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    // Categoria classificada da transação.
    // Exemplos: alimentacao, transporte, entretenimento, saude, moradia, outros.
    // Pode ser nulo antes de ser classificado pelo modelo de ML.
    @Column
    private String categoria;

    // Data e hora em que a transação foi registrada. Preenchido automaticamente.
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    // Usuário dono desta transação.
    // Relacionamento Many-to-One: muitas transações pertencem a um único usuário.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @PrePersist
    public void prePersist() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }
}
