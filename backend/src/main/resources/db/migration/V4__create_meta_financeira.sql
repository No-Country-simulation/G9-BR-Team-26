-- Cria a tabela de metas financeiras definidas pelo usuário.
-- Uma meta é sempre relacionada ao nível de endividamento (métrica mais objetiva
-- e já presente em toda análise financeira).

CREATE TABLE meta_financeira (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id            BIGINT         NOT NULL,
    descricao             VARCHAR(255)   NOT NULL,
    endividamento_alvo    INT            NOT NULL,
    data_alvo             DATE           NOT NULL,
    criado_em             TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    concluida             BOOLEAN        NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_meta_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);