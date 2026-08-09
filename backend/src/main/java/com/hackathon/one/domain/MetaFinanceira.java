package com.hackathon.one.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "meta_financeira")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String descricao;

    // Nível de endividamento que o usuário quer atingir (ex: 15 = 15%).
    @Column(name = "endividamento_alvo", nullable = false)
    private Integer endividamentoAlvo;

    @Column(name = "data_alvo", nullable = false)
    private LocalDate dataAlvo;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    @Builder.Default
    private Boolean concluida = false;

    @PrePersist
    public void prePersist() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }
}