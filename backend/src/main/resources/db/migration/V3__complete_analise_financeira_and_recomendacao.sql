-- Adiciona os campos pendentes da tabela analise_financeira (TODO da etapa 1)
-- e completa a tabela recomendacao com o texto e o relacionamento com analise_financeira.

ALTER TABLE analise_financeira
    ADD COLUMN usuario_id           BIGINT         NOT NULL,
    ADD COLUMN renda_mensal         DECIMAL(15, 2) NOT NULL,
    ADD COLUMN nivel_endividamento  INT            NOT NULL,
    ADD COLUMN frequencia_poupanca  VARCHAR(50)    NOT NULL,
    ADD COLUMN perfil_financeiro    VARCHAR(50)    NOT NULL,
    ADD COLUMN probabilidade        DOUBLE         NOT NULL,
    ADD COLUMN criado_em            TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    ADD CONSTRAINT fk_analise_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id);

ALTER TABLE recomendacao
    ADD COLUMN texto      VARCHAR(500) NOT NULL,
    ADD COLUMN analise_id BIGINT       NOT NULL,
    ADD CONSTRAINT fk_recomendacao_analise
        FOREIGN KEY (analise_id) REFERENCES analise_financeira (id);