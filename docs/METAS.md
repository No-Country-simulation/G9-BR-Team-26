# Análise de Comportamento Financeiro — Plano de Metas do Hackathon

Sistema inteligente para análise de comportamento financeiro, classificação de despesas, perfil financeiro do usuário e geração de recomendações personalizadas, com backend em **Spring Boot**, banco **MySQL**, orquestração via **Docker Compose** e modelo de **Ciência de Dados** integrado via API.

> 📖 **Mais detalhes sobre entradas, saídas e códigos de erro de cada endpoint estão em [`docs/PLANO-ENDPOINTS.md`](./docs/PLANO-ENDPOINTS.md).**

---

## 📋 Visão Geral

| Item | Descrição |
|---|---|
| Objetivo | MVP funcional de análise financeira com classificação de despesas, perfil de risco e recomendações |
| Entrada | Renda mensal, nível de endividamento, frequência de poupança, transações |
| Saída | JSON com perfil financeiro, resumo de gastos e recomendações |
| Stack Backend | Spring Boot, MySQL, Docker Compose |
| Stack Dados | Python, Pandas, Scikit-Learn, joblib |
| Integração obrigatória | Pelo menos 1 serviço OCI |

---

## 🔧 Bloco Backend (Spring Boot + MySQL + Docker Compose)

### 1. Setup do projeto e modelagem de dados
**Endpoints tratados nesta etapa:** nenhum ainda (fundação para todos os endpoints)
- [ ] Criar projeto Spring Boot (Spring Web, Spring Data JPA, MySQL Driver, Validation, Lombok)
- [ ] Definir `docker-compose.yml` com serviços `app` (Spring Boot) + `mysql` (volume persistente + variáveis de ambiente)
- [ ] Modelar entidades: `Usuario`, `Transacao`, `AnaliseFinanceira`, `Recomendacao`
- [ ] Criar `application.yml` com profiles (`dev` / `docker`) apontando para o MySQL do container
- [ ] Rodar migração inicial (Flyway ou `ddl-auto: update` para o MVP)

### 2. Autenticação (login/signup)
**Endpoints tratados nesta etapa:**
- `POST /auth/signup`
- `POST /auth/login`
- [ ] Implementar `POST /auth/signup` (cadastro de usuário, validação de e-mail duplicado)
- [ ] Implementar `POST /auth/login` (autenticação e emissão de JWT)
- [ ] Configurar Spring Security com filtro JWT para proteger as rotas autenticadas

### 3. Estrutura em camadas e CRUD base
**Endpoints tratados nesta etapa:**
- `POST /transacoes` *(criação/persistência)*
- `GET /transacoes/{usuarioId}`
- `GET /usuarios/{id}`
- [ ] Criar camadas: `Controller` → `Service` → `Repository` → `DTO` (nunca expor entidade diretamente)
- [ ] Endpoints CRUD básicos: `POST /transacoes`, `GET /transacoes/{usuarioId}`, `GET /usuarios/{id}`
- [ ] Configurar `ModelMapper` ou mapeamento manual DTO ↔ Entity
- [ ] Testar tudo via Postman/Insomnia antes de avançar

### 4. Endpoint principal de análise financeira
**Endpoints tratados nesta etapa:**
- `POST /analise-financeira`
- `POST /transacoes/classificar`
- [ ] Implementar `POST /analise-financeira` conforme exemplo de entrada/saída do projeto
- [ ] Implementar `POST /transacoes/classificar` para classificação isolada de despesas
- [ ] Integrar com o modelo de ML (treinado em Python, `.joblib`) — estratégia recomendada: expor o modelo como microserviço Python (FastAPI/Flask) e consumir via `RestTemplate`/`WebClient`
- [ ] Montar resposta agregando: classificação de despesas + perfil financeiro + recomendações, no formato JSON exigido

### 5. Validação, tratamento de erros e segurança
**Endpoints tratados nesta etapa:** validação/tratamento de erro transversal a todos os endpoints acima (`/auth/*`, `/transacoes/*`, `/analise-financeira`)
- [ ] Validar entrada com `@Valid` + Bean Validation (`@NotNull`, `@Positive`, etc.)
- [ ] Criar `@ControllerAdvice` global para tratar exceções (400, 404, 500 padronizados)
- [ ] Reforçar segurança dos endpoints autenticados com Spring Security
- [ ] Logs estruturados para depuração durante a demo

### 6. Histórico e processamento em lote *(opcional)*
**Endpoints tratados nesta etapa:**
- `GET /analises/historico`
- `GET /analises/{id}`
- `POST /transacoes/lote`
- [ ] Implementar `GET /analises/historico` (listagem das análises do usuário autenticado)
- [ ] Implementar `GET /analises/{id}` (detalhe de uma análise específica)
- [ ] Implementar `POST /transacoes/lote` para upload e processamento de CSV
- [ ] Persistir cada `AnaliseFinanceira` gerada para alimentar o histórico

### 7. Documentação, testes e OCI
**Endpoints tratados nesta etapa:** documentação Swagger de todos os endpoints (`/auth/*`, `/transacoes/*`, `/analise-financeira`, `/analises/*`)
- [ ] Documentar endpoints com Swagger/OpenAPI (`springdoc-openapi`)
- [ ] Testes automatizados mínimos (JUnit + MockMvc) nos endpoints críticos (`/analise-financeira`, `/auth/login`)
- [ ] Integrar OCI Object Storage (upload do modelo `.joblib`, dos dados, ou persistência de relatórios)
- [ ] Ajustar `docker-compose.yml` final para subir tudo com um comando único (app + mysql + serviço de ML)

---

## 📊 Bloco Ciência de Dados

### 1. Construção do dataset e EDA
**Endpoint relacionado:** nenhum diretamente (etapa de preparação, base para `/analise-financeira` e `/transacoes/classificar`)
- [ ] Definir estrutura do dataset: descrição, valor, categoria, renda mensal, endividamento, frequência de poupança
- [ ] Gerar dados simulados/sintéticos (ou combinar com base pública), garantindo variedade de perfis (saudável, em observação, em risco)
- [ ] EDA: distribuição de categorias, outliers em valores, correlação entre renda/endividamento/poupança

### 2. Pré-processamento e engenharia de atributos
**Endpoint relacionado:** nenhum diretamente (alimenta o modelo usado em `/transacoes/classificar` e `/analise-financeira`)
- [ ] Tratamento de texto das descrições de transação (normalização, remoção de acentos/stopwords) para classificação de categoria
- [ ] Criar atributos derivados: % de gastos por categoria sobre a renda, taxa de poupança, razão dívida/renda
- [ ] Encoding de variáveis categóricas (frequência de poupança: Baixa/Média/Alta → ordinal)

### 3. Treinamento dos modelos
**Endpoint relacionado:** modelos consumidos por `POST /transacoes/classificar` (Modelo 1) e `POST /analise-financeira` (Modelo 2)
- [ ] Modelo 1: classificador de categoria de despesa (texto → categoria) — ex. TF-IDF + Naive Bayes/SVM
- [ ] Modelo 2: classificador de perfil financeiro (features numéricas → Saudável/Em observação/Em risco) — ex. Random Forest/Logistic Regression
- [ ] Testar múltiplos algoritmos e comparar desempenho

### 4. Avaliação e serialização
**Endpoint relacionado:** artefatos consumidos por `POST /transacoes/classificar` e `POST /analise-financeira`
- [ ] Métricas: acurácia, precisão, recall, F1 (matriz de confusão para o perfil financeiro, considerando possível desbalanceamento)
- [ ] Validação cruzada para evitar overfitting nos dados simulados
- [ ] Serializar ambos os modelos com `joblib` (`modelo_categoria.joblib`, `modelo_perfil.joblib`)

### 5. Geração de recomendações e exposição do modelo
**Endpoint relacionado:** `POST /analise-financeira` (microserviço Python consumido pelo backend Spring Boot)
- [ ] Criar lógica de regras (ou modelo simples) para gerar recomendações a partir do perfil + resumo de gastos
- [ ] Expor os modelos via API própria (FastAPI recomendado) com endpoint que recebe transações e devolve classificação + perfil + recomendações
- [ ] Alinhar contrato JSON com o time de backend (mesmo schema do exemplo do projeto) para integração direta

---

## 🔗 Contrato de Integração (Backend ↔ Ciência de Dados)

**Endpoint:** `POST /analise-financeira`

**Entrada:**
```json
{
  "renda_mensal": 4500,
  "nivel_endividamento": 25,
  "frequencia_poupanca": "Media",
  "transacoes": [
    { "descricao": "Supermercado", "valor": 420 },
    { "descricao": "Combustivel", "valor": 300 },
    { "descricao": "Streaming", "valor": 40 }
  ]
}
```

**Saída:**
```json
{
  "perfil_financeiro": "Em observacao",
  "probabilidade": 0.82,
  "resumo_gastos": {
    "alimentacao": 420,
    "transporte": 300,
    "entretenimento": 40
  },
  "recomendacoes": [
    "Monitorar gastos recorrentes de entretenimento",
    "Aumentar reserva financeira mensal"
  ]
}
```

> ℹ️ Este é apenas um exemplo resumido do contrato principal. A especificação completa de **todos** os endpoints (`/auth/signup`, `/auth/login`, `/auth/refresh`, `/transacoes/classificar`, `/analise-financeira`, `/analises/historico`, `/analises/{id}`, `/transacoes/lote`) — com entradas, saídas e códigos de erro — está documentada em [`docs/PLANO-ENDPOINTS.md`](./docs/PLANO-ENDPOINTS.md).

---

## ✅ Requisitos Mínimos do MVP
- Modelo treinado e carregado corretamente
- Validação de entrada
- Classificação funcional das transações
- Análise de perfil financeiro
- Geração de recomendações
- API documentada
- Integração com pelo menos 1 serviço OCI
- Mínimo de três exemplos reais de utilização

---

## 💡 Recursos Opcionais (se sobrar tempo)
- Dashboard financeiro
- Visualização da evolução financeira
- Processamento em lote via CSV (`POST /transacoes/lote`)
- Histórico de análises (`GET /analises/historico`, `GET /analises/{id}`)
- Alertas de gastos elevados
- Testes automatizados mais completos
- Exportação de relatórios
- Explicabilidade dos modelos