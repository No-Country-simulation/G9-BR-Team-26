
package com.hackathon.one.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recomendacao")
@Getter
@Setter
@NoArgsConstructor
public class Recomendacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String texto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analise_id", nullable = false)
    private AnaliseFinanceira analiseFinanceira;
}