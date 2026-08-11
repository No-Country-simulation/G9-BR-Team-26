-- Adiciona a coluna de score na análise financeira (antes só calculado, não persistido).
ALTER TABLE analise_financeira
    ADD COLUMN score INT NULL;

-- Adiciona o endividamento "de partida" na meta, capturado no momento da criação.
-- Usado para calcular o progresso de forma estável (não oscila se o endividamento
-- piorar entre duas análises, diferente de usar sempre o valor "atual").
ALTER TABLE meta_financeira
    ADD COLUMN endividamento_inicial INT NULL;