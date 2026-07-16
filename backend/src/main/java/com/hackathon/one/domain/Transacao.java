package com.hackathon.one.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transacao")
@Getter
@Setter
@NoArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Adicionar os campos restantes (descricao, valor, categoria, usuario_id) quando necessário
}
