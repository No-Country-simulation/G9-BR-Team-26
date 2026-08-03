# Data Science — Smart Finance

Módulo responsável por treinar, avaliar e serializar os modelos de Machine Learning usados pelo backend Spring Boot do projeto **Smart Finance**.

## Modelos

| Modelo | Objetivo | Algoritmo | Artefatos gerados |
|---|---|---|---|
| **Modelo 1 — Categoria** | Classificar a categoria de uma transação a partir da descrição textual (ex: "Uber" → `Transporte`) | TF-IDF + Multinomial Naive Bayes | `modelo_categoria.joblib`, `tfidf.joblib` |
| **Modelo 2 — Perfil Financeiro** | Classificar o perfil financeiro do usuário (`Saudável` / `Em observação` / `Em risco`) a partir de dados socioeconômicos e financeiros | One-Hot Encoding + Random Forest (via `Pipeline`) | `modelo_perfil.joblib`, `feature_importance.csv` |

## Estrutura de pastas

```
data-science/
├── data/
│   ├── eda.ipynb              # Exploração e verificação de qualidade dos dados brutos
│   ├── raw/                   # Datasets originais (usuarios.csv, transacoes.csv)
│   └── processed/             # Dados tratados, features derivadas e datasets prontos para treino
├── notebooks/
│   ├── sandbox/                # Tratamento de variáveis e engenharia de atributos
│   │   ├── TratamentoVariaveis.ipynb
│   │   └── EngenhariaAtributos.ipynb
│   └── training/                # Treinamento, avaliação e serialização dos modelos
│       ├── TreinamentoCategoria.ipynb
│       └── TreinamentoPerfil.ipynb
├── models/                    # Artefatos serializados (.joblib) consumidos pela API
│   ├── modelo_categoria.joblib
│   ├── tfidf.joblib
│   ├── modelo_perfil.joblib
│   └── feature_importance.csv
├── api/                       # Serviço FastAPI que expõe os modelos ao backend Spring Boot
│   ├── main.py
│   ├── schemas.py
│   ├── predictor.py
│   └── rules.py
└── requirements.txt
```

## Fluxo do pipeline

1. `data/eda.ipynb` — exploração inicial e verificação de qualidade dos dados brutos.
2. `notebooks/sandbox/TratamentoVariaveis.ipynb` — limpeza e tratamento de tipos/inconsistências.
3. `notebooks/sandbox/EngenhariaAtributos.ipynb` — criação de atributos derivados e geração dos datasets prontos para treino (`data/processed/dataset_modelo_categoria.csv`, `data/processed/dataset_modelo_perfil.csv`).
4. `notebooks/training/TreinamentoCategoria.ipynb` — treino, avaliação e serialização do Modelo 1.
5. `notebooks/training/TreinamentoPerfil.ipynb` — treino, avaliação e serialização do Modelo 2.
6. `api/predictor.py` — carrega os artefatos de `models/` e expõe as funções de predição consumidas pela API.

## Métricas obtidas

- **Modelo 1 (Categoria):** Accuracy ≈ 98,4% · F1-macro ≈ 98,4% (validação cruzada: F1-macro médio ≈ 98,1%, desvio padrão ≈ 0,001).
- **Modelo 2 (Perfil Financeiro):** Accuracy ≈ 100% · F1-macro ≈ 100%.

> **Observação importante sobre o Modelo 2:** as variáveis mais relevantes identificadas (`nivel_endividamento`, `score_credito`, `percentual_renda_investida`) são as mesmas utilizadas na definição original do rótulo `perfil_financeiro` no dataset sintético. Ou seja, o modelo reproduz com alta fidelidade a lógica de negócio que já originou o rótulo, e não necessariamente descobre um padrão novo e independente. Isso é esperado dado como o dataset foi construído, e fica registrado aqui como limitação/transparência do experimento — não é um bug do pipeline.

## Como rodar localmente

```bash
pip install -r requirements.txt
```

Os notebooks usam caminhos relativos partindo da própria pasta onde estão (`../../data`, `../../models`), então devem ser executados a partir de dentro de `notebooks/sandbox/` ou `notebooks/training/`, respectivamente, com Jupyter/VS Code apontando o kernel para o ambiente onde as dependências foram instaladas.
