# 📊 Smart Finance - Módulo de Data Science & API FastAPI

Este diretório contém os componentes de **Machine Learning**, notebooks de treinamento e a **API FastAPI** responsável por servir os modelos de Inteligência Artificial para o backend Spring Boot do projeto **Smart Finance**.

---

## 🏗️ Arquitetura de Pastas de Data Science

O módulo foi estruturado para separar o fluxo de **Exploração/Treinamento**, **Serialização de Artefatos** e **Serviço de Inferência (API)**:

```bash
data-science/
├── notebooks/                # Jupyter Notebooks de EDA e treinamento (.ipynb)
│   ├── 01_treino_classificador_categoria.ipynb
│   └── 02_treino_perfil_financeiro.ipynb
├── models/                   # Artefatos serializados (.joblib) e metadados
│   ├── modelo_categoria.joblib
│   ├── modelo_perfil.joblib
│   └── classes.json
├── api/                      # Aplicação FastAPI em Python
│   ├── main.py               # Instância do FastAPI, rotas e eventos de startup
│   ├── schemas.py            # Modelos de entrada/saída (Pydantic)
│   ├── predictor.py          # Carregador de artefatos .joblib e lógica de inferência
│   └── rules.py              # Motor de regras de recomendação financeira
├── requirements.txt          # Dependências Python (fastapi, uvicorn, scikit-learn, joblib, pydantic)
├── FLUXO.md                  # Documentação detalhada dos fluxos de dados
├── PLANOS.md                 # Planejamento das fases de desenvolvimento de ML
└── README.md                 # Visão geral deste módulo
```

---

## 🔄 Fluxo de Integração: Entrada (Spring) ➔ FastAPI ➔ Modelo IA ➔ Saída (Spring)

A API FastAPI atua como uma ponte de alta performance entre as requisições enviadas pelo backend Spring Boot e a inferência dos modelos `.joblib`.

```mermaid
graph TD
    Spring[Spring Boot Backend] -->|1. HTTP POST Request| FastAPI[FastAPI REST API (api/main.py)]
    FastAPI -->|2. Valida payload Pydantic| Schemas[Schemas (api/schemas.py)]
    Schemas -->|3. Passa dados numéricos/texto| Predictor[Predictor (api/predictor.py)]
    Predictor -->|4. Executa predict()| ML[Modelos ML (.joblib)]
    Predictor -->|5. Se perfil: avalia regras| Rules[Rules Engine (api/rules.py)]
    Predictor -->|6. Retorna predição| FastAPI
    FastAPI -->|7. Resposta JSON estruturada| Spring
```

---

## 📌 Detalhamento dos Endpoints FastAPI

### 1. Endpoint: `POST /classificar`
- **Consumido por**: Endpoint Spring `POST /transacoes/classificar`
- **Objetivo**: Classificar a descrição de uma transação individual em uma categoria financeira.

#### Fluxo Entrada ➔ FastAPI ➔ Modelo:
1. Spring Boot envia: `{"descricao": "Ifood Burger King", "valor": 45.90}`.
2. `api/main.py` recebe a requisição e valida via `TransacaoInput` (em `api/schemas.py`).
3. `api/predictor.py` aplica o vetorizador **TF-IDF** na descrição e executa o modelo de classificação (`modelo_categoria.joblib`).
4. Retorna a categoria identificada (ex: `alimentacao`) e o nível de confiança (ex: `0.95`).

#### Exemplo de Payload (Request / Response):
**Requisição (Spring -> FastAPI):**
```json
{
  "descricao": "Uber Trip",
  "valor": 28.50
}
```

**Resposta (FastAPI -> Spring):**
```json
{
  "categoria": "transporte",
  "confianca": 0.94
}
```

---

### 2. Endpoint: `POST /analise-financeira`
- **Consumido por**: Endpoint Spring `POST /analise-financeira`
- **Objetivo**: Avaliar o comportamento global do usuário, predizer seu perfil financeiro (`Saudavel`, `Em observacao`, `Em risco`) e fornecer recomendações.

#### Fluxo Entrada ➔ FastAPI ➔ Modelo:
1. Spring Boot busca as transações do usuário no MySQL, consolida os totais por categoria e envia os dados consolidados.
2. `api/main.py` valida o payload via `AnaliseInput` (em `api/schemas.py`).
3. `api/predictor.py` passa as features agregadas ao modelo de perfil (`modelo_perfil.joblib`), obtendo a classe e a probabilidade de risco.
4. `api/rules.py` combina a classe predita com a proporção de despesas para gerar uma lista de recomendações personalizadas.
5. Retorna o perfil, probabilidade, resumo de gastos e recomendações.

#### Exemplo de Payload (Request / Response):
**Requisição (Spring -> FastAPI):**
```json
{
  "renda_mensal": 4500.0,
  "nivel_endividamento": 25.0,
  "frequencia_poupanca": "Media",
  "gasto_alimentacao": 420.0,
  "gasto_transporte": 300.0,
  "gasto_lazer": 150.0,
  "gasto_moradia": 1200.0,
  "gasto_saude": 0.0,
  "gasto_educacao": 0.0,
  "gasto_servicos": 50.0,
  "gasto_outros": 0.0
}
```

**Resposta (FastAPI -> Spring):**
```json
{
  "perfil_financeiro": "Em observacao",
  "probabilidade": 0.82,
  "resumo_gastos": {
    "alimentacao": 420.0,
    "transporte": 300.0,
    "lazer": 150.0,
    "moradia": 1200.0,
    "saude": 0.0,
    "educacao": 0.0,
    "servicos": 50.0,
    "outros": 0.0
  },
  "recomendacoes": [
    "Monitorar gastos recorrentes com lazer para aumentar a margem mensal.",
    "Criar ou expandir uma reserva de emergência equivalente a 3 meses de despesas."
  ]
}
```

---

## 🛠️ Como Executar a API Localmente

```bash
# 1. Navegue até este diretório
cd data-science

# 2. Instale as dependências
pip install -r requirements.txt

# 3. Inicie o servidor FastAPI
uvicorn api.main:app --reload --port 8000
```

A documentação interativa OpenAPI (Swagger UI) estará disponível em: `http://localhost:8000/docs`.
