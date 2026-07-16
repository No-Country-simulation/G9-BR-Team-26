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

    // TODO: Adicionar os campos restantes
}
