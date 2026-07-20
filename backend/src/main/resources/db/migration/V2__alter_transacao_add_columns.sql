-- Adiciona os campos da tabela transacao que estavam pendentes (TODO da etapa 1)
ALTER TABLE transacao
    ADD COLUMN descricao  VARCHAR(255)   NOT NULL DEFAULT '',
    ADD COLUMN valor      DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    ADD COLUMN categoria  VARCHAR(100)   NULL,
    ADD COLUMN criado_em  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN usuario_id BIGINT         NOT NULL DEFAULT 0,
    ADD CONSTRAINT fk_transacao_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id);

-- Remove os defaults temporários que foram usados para permitir o ALTER TABLE sem dados nulos
ALTER TABLE transacao
    ALTER COLUMN descricao DROP DEFAULT,
    ALTER COLUMN valor     DROP DEFAULT,
    ALTER COLUMN usuario_id DROP DEFAULT;
