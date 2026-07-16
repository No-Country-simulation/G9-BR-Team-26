package com.hackathon.one.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "analise_financeira")
@Getter
@Setter
@NoArgsConstructor
public class AnaliseFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Adicionar os campos restantes
}
