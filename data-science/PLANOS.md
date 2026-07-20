# Planejamento simplificado (data-science)

Objetivo: suportar dois modelos separados que o backend consome:

- Modelo A — Classificação de transações (por transação): retorna `categoria` e `confianca`.
- Modelo B — Análise financeira e recomendações (por usuário): retorna `perfil_financeiro`, `probabilidade`, `resumo_gastos` e `recomendacoes`.

Estrutura mínima de pastas

- `data/raw/` — CSVs brutos (exportados do banco)
- `data/processed/` — CSVs limpos e prontos para treino
- `src/models/` — scripts de treino/avaliação/serialização para A e B
- `models/` — artefatos serializados (`cat_v1.joblib`, `rec_v1.joblib`) e `classes.json`
- `data/labeling/` — arquivos auxiliares de rotulagem manual
- `data/experiments/` — histórico de execuções e recomendações

Principais CSVs (tabelas) e finalidades

1) `data/raw/transactions.csv` — histórico de transações (treino Modelo A)
- `transacao_id` (INT)
- `descricao` (STRING)
- `valor` (DECIMAL)
- `usuario_id` (INT)
- `criado_em` (ISO DATETIME)
- `categoria` (STRING, NULLABLE) — label alvo

Uso: treinar o classificador; também serve para agregar por usuário para Modelo B.

2) `data/processed/transacoes_for_training.csv` — versão limpa/tokenizada de `transactions.csv` pronta para treino.

3) `data/raw/users_profiles.csv` — perfil declarativo (opcional, treina Modelo B)
- `usuario_id` (INT)
- `renda_mensal` (DECIMAL)
- `nivel_endividamento` (DECIMAL)
- `frequencia_poupanca` (STRING: Baixa/Media/Alta)
- `classe_perfil` (STRING, NULLABLE)

4) `data/processed/user_aggregates.csv` — agregado por usuário (entrada para Modelo B)
- `usuario_id` (INT)
- `renda_mensal` (DECIMAL)
- `nivel_endividamento` (DECIMAL)
- `frequencia_poupanca` (STRING)
- `total_alimentacao` (DECIMAL)
- `total_transporte` (DECIMAL)
- `total_entretenimento` (DECIMAL)
- `total_outros` (DECIMAL)
- `proporcao_entretenimento` (DECIMAL)
- `numero_transacoes` (INT)
- `classe_perfil` (STRING, NULLABLE)

5) `data/labeling/transacao_labels.csv` — (transacao_id, categoria) para fluxos de rotulagem manual.

6) `data/experiments/recommendations_history.csv` — log em produção de recomendações para feedback.
- `registro_id` (INT), `usuario_id` (INT), `timestamp` (ISO), `recomendacao` (STRING), `motivo` (STRING), `modelo_versao` (STRING)


Mapeamento direto para endpoints do backend

- `POST /transacoes/classificar`
  - Input esperado: lista de objetos com `descricao` e `valor`.
  - Offline/batch: CSV com as colunas `descricao,valor,usuario_id,transacao_id` (sem `categoria`).
  - Produz saída: `transacoes_classificadas` com `categoria` e `confianca`.

- `POST /analise-financeira` (MVP)
  - Input esperado: `renda_mensal`, `nivel_endividamento`, `frequencia_poupanca`, `transacoes` (lista de transações recentes).
  - Internamente: o serviço pode montar um `user_aggregate` a partir das transações e dos perfis; Modelo B consome esse agregado e retorna `perfil_financeiro`, `probabilidade`, `resumo_gastos` e `recomendacoes`.

Formato de saída (contratos)

- Modelo A (por item):
```json
{
  "descricao": "...",
  "valor": 123.45,
  "categoria": "alimentacao",
  "confianca": 0.92
}
```

- Modelo B (análise completa):
```json
{
  "perfil_financeiro": "Em observacao",
  "probabilidade": 0.82,
  "resumo_gastos": { "alimentacao": 420, "transporte": 300, "entretenimento": 40 },
  "recomendacoes": ["Monitorar gastos recorrentes de entretenimento", "Aumentar reserva financeira mensal"]
}
```

Treino resumido (passos mínimos)

- Modelo A
  1. Preparar `data/processed/transacoes_for_training.csv` a partir de `transactions.csv`.
  2. TF-IDF + classifier (LogisticRegression/MultinomialNB/SVM). Validação estratificada.
  3. Salvar pipeline com `joblib` e `models/classes.json`.

- Modelo B
  1. Gerar `data/processed/user_aggregates.csv` (agregação por `usuario_id`).
  2. Criar features (gastos por categoria, razão gasto/renda, frequência de transações).
  3. Treinar classificador de perfil (RandomForest/LogisticRegression) e um módulo de regras/score para recomendações.
  4. Salvar artefatos em `models/`.

Boas práticas rápidas

- Codificação: UTF-8; CSV com vírgula; escape de aspas para campos textuais.
- Versionamento: salvar `models/metadata.json` com `version` e `trained_at`.
- Produtos: expor os dois modelos via FastAPI (rota para classificação em lote e rota para análise completa).
- Monitoramento: armazenar `recommendations_history.csv` para feedback humano e re-treinos.

Próximos passos (posso implementar)

1. Gerar `requirements.txt`, `train.sh` e um `src/models/train_category.py` mínimo (TF-IDF + LogisticRegression) que salva `models/cat_v1.joblib`.
2. Gerar `src/models/prepare_aggregates.py` que produz `data/processed/user_aggregates.csv` a partir de `data/raw/transactions.csv` e `data/raw/users_profiles.csv`.

