# API Smart Finance AI

Esta API expõe dois endpoints para inferência de modelos de machine learning e comunica-se com os artefatos treinados armazenados na pasta de modelos.

## Como funciona a lógica de comunicação

O fluxo é simples e segue esta ordem:

1. A aplicação inicia no arquivo main.py.
2. No ciclo de vida da aplicação, o sistema carrega os modelos `.joblib` armazenados em `data-science/models`.
3. O `ModelPredictor` é responsável por carregar os arquivos e executar previsões.
4. O `PredictionService` atua como camada intermediária entre os controllers e o modelo.
5. Os controllers recebem a requisição HTTP, validam o payload e delegam a execução ao service.
6. O service chama o modelo e retorna uma resposta estruturada em JSON.

### Fluxo detalhado

- `main.py`
  - inicializa a aplicação FastAPI;
  - carrega os modelos na inicialização;
  - registra os roteadores de saúde, classificação e análise financeira.

- `controller/transaction_controller.py`
  - recebe requisições para o endpoint `/classificar`;
  - encaminha a solicitação ao `PredictionService`.

- `controller/financial_controller.py`
  - recebe requisições para o endpoint `/analise-financeira`;
  - encaminha a solicitação ao `PredictionService`.

- `service/prediction_service.py`
  - valida se os modelos necessários estão carregados;
  - chama funções de predição do `ModelPredictor`;
  - transforma o resultado em DTOs e retorna para a API.

- `scripts/predictor.py`
  - realiza a inferência real com os modelos treinados;
  - usa o texto da transação e/ou os dados do perfil financeiro para gerar a resposta.

## Endpoints disponíveis

### 1. Health check
- Endpoint: `/health`
- Verifica se a API está funcionando e se os modelos foram carregados.

### 2. Classificação de transação
- Endpoint: `/classificar`
- Recebe descrição e valor opcional da transação.
- Retorna a categoria prevista e o nível de confiança.

### 3. Análise de perfil financeiro
- Endpoint: `/analise-financeira`
- Recebe renda mensal, nível de endividamento e frequência de poupança.
- Retorna o perfil financeiro previsto com uma probabilidade.

## Como compilar e executar a API

### 🐳 Via Docker (Recomendado — Porta 7070)

A partir da pasta `data-science`:

```bash
# 1. Gerar a imagem Docker
docker build -t smart-finance-ds .

# 2. Executar o container na porta 7070
docker run -d -p 7070:7070 --name datascience-api smart-finance-ds
```

### 🐍 Via Python Local

1. Instalar dependências (a partir da pasta `data-science`):
   ```bash
   pip install -r requirements.txt
   ```

2. Executar a API na porta 7070 (a partir da pasta `data-science/api`):
   ```bash
   uvicorn main:app --host 0.0.0.0 --port 7070 --reload
   ```

### Documentação Swagger

Após subir a aplicação, acesse:

```text
http://localhost:7070/docs
```

## Observações importantes

- Os modelos precisam estar disponíveis na pasta `data-science/models`.
- A API depende de arquivos `.joblib` para realizar predições.
- O token de autenticação é verificado pelos controllers através da dependência de segurança.
- Se algum modelo não for carregado, a API retorna erro `503`.
