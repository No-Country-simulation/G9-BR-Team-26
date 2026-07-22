package com.hackathon.one.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "analise_financeira")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnaliseFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuário dono desta análise.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Dados de entrada que o usuário enviou (guardados para histórico/auditoria).
    @Column(name = "renda_mensal", nullable = false, precision = 15, scale = 2)
    private BigDecimal rendaMensal;

    @Column(name = "nivel_endividamento", nullable = false)
    private Integer nivelEndividamento;

    @Column(name = "frequencia_poupanca", nullable = false)
    private String frequenciaPoupanca;

    // Resultado calculado (hoje, mock; no futuro, vindo do modelo de ML).
    @Column(name = "perfil_financeiro", nullable = false)
    private String perfilFinanceiro;

    @Column(nullable = false)
    private Double probabilidade;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    // Uma análise tem várias recomendações. "mappedBy" indica que quem controla
    // o relacionamento é o campo "analiseFinanceira" lá na classe Recomendacao.
    // cascade = ALL: ao salvar/deletar a análise, as recomendações ligadas seguem junto.
    @OneToMany(mappedBy = "analiseFinanceira", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Recomendacao> recomendacoes = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }

    // Método de conveniência: adiciona uma recomendação já ligando os dois lados
    // do relacionamento (evita esquecer de setar o lado inverso, erro comum em JPA).
    public void adicionarRecomendacao(String texto) {
        Recomendacao recomendacao = new Recomendacao();
        recomendacao.setTexto(texto);
        recomendacao.setAnaliseFinanceira(this);
        this.recomendacoes.add(recomendacao);
    }
}
