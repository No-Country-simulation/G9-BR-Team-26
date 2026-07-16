# Análise de Endpoints — API de Análise Financeira

## Visão Geral

Este documento define os endpoints da API REST para o sistema de análise de comportamento financeiro, incluindo autenticação (login/signup) e os endpoints obrigatórios do MVP do hackathon.

Todas as rotas retornam e recebem dados em **JSON**. As rotas marcadas como autenticadas exigem o header:

```
Authorization: Bearer <access_token>
```

---

## 1. Autenticação

### `POST /auth/signup`

Cadastra um novo usuário no sistema.

**Autenticado:** Não

**Entrada:**
```json
{
  "nome": "Natan Silva",
  "email": "natan@email.com",
  "senha": "SenhaForte123"
}
```

**Saída (201 Created):**
```json
{
  "id": "usr_001",
  "nome": "Natan Silva",
  "email": "natan@email.com",
  "criado_em": "2026-07-15T10:00:00Z"
}
```

**Erros possíveis:**
| Código | Motivo |
|---|---|
| 400 | Dados inválidos (campos ausentes, e-mail mal formatado) |
| 409 | E-mail já cadastrado |

---

### `POST /auth/login`

Autentica um usuário existente e retorna um token de acesso.

**Autenticado:** Não

**Entrada:**
```json
{
  "email": "natan@email.com",
  "senha": "SenhaForte123"
}
```

**Saída (200 OK):**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIs...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

**Erros possíveis:**
| Código | Motivo |
|---|---|
| 401 | Credenciais inválidas |

> Recomendação: usar JWT com Spring Security, já que o edital sugere Java + Spring Boot para o backend.

---

### `POST /auth/refresh` *(opcional, recomendado)*

Renova o token de acesso usando um refresh token.

**Autenticado:** Não (usa refresh token no corpo)

**Entrada:**
```json
{
  "refresh_token": "dGhpcyBpcyBhIHJlZnJlc2g..."
}
```

**Saída (200 OK):**
```json
{
  "access_token": "novo_token_aqui",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

---

## 2. Classificação de Transações

### `POST /transacoes/classificar`

Classifica automaticamente uma lista de transações em categorias financeiras, sem gerar a análise completa de perfil.

**Autenticado:** Sim

**Entrada:**
```json
{
  "transacoes": [
    { "descricao": "Supermercado Extra", "valor": 420.00 },
    { "descricao": "Uber", "valor": 35.50 }
  ]
}
```

**Saída (200 OK):**
```json
{
  "transacoes_classificadas": [
    {
      "descricao": "Supermercado Extra",
      "valor": 420.00,
      "categoria": "Alimentação",
      "confianca": 0.94
    },
    {
      "descricao": "Uber",
      "valor": 35.50,
      "categoria": "Transporte",
      "confianca": 0.88
    }
  ]
}
```

**Erros possíveis:**
| Código | Motivo |
|---|---|
| 400 | Lista de transações vazia ou campos ausentes |

---

## 3. Análise Financeira Completa

### `POST /analise-financeira`

Endpoint principal do MVP. Recebe os dados financeiros do usuário e retorna perfil, resumo de gastos e recomendações.

**Autenticado:** Sim

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

**Saída (200 OK):**
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

**Erros possíveis:**
| Código | Motivo |
|---|---|
| 400 | `renda_mensal` ausente, negativa, ou lista de transações vazia |
| 422 | Formato de entrada inválido |

---

## 4. Histórico de Análises *(opcional)*

### `GET /analises/historico`

Lista as análises anteriores do usuário autenticado.

**Autenticado:** Sim

**Saída (200 OK):**
```json
{
  "analises": [
    {
      "id": "an_001",
      "data": "2026-07-10T14:22:00Z",
      "perfil_financeiro": "Em observacao",
      "resumo_gastos": { "alimentacao": 420, "transporte": 300 }
    }
  ]
}
```

---

### `GET /analises/{id}`

Retorna o detalhe completo de uma análise específica.

**Autenticado:** Sim

**Saída (200 OK):**
```json
{
  "id": "an_001",
  "data": "2026-07-10T14:22:00Z",
  "renda_mensal": 4500,
  "nivel_endividamento": 25,
  "perfil_financeiro": "Em observacao",
  "probabilidade": 0.82,
  "resumo_gastos": { "alimentacao": 420, "transporte": 300, "entretenimento": 40 },
  "recomendacoes": [
    "Monitorar gastos recorrentes de entretenimento",
    "Aumentar reserva financeira mensal"
  ]
}
```

**Erros possíveis:**
| Código | Motivo |
|---|---|
| 404 | Análise não encontrada |

---

## 5. Processamento em Lote *(opcional)*

### `POST /transacoes/lote`

Recebe um arquivo CSV com múltiplas transações e retorna um resumo processado.

**Autenticado:** Sim

**Entrada:** `multipart/form-data` contendo um arquivo `.csv`

**Saída (200 OK):**
```json
{
  "total_processadas": 150,
  "resumo_por_categoria": {
    "Alimentação": 1200,
    "Transporte": 800
  },
  "perfil_financeiro": "Saudavel"
}
```

**Erros possíveis:**
| Código | Motivo |
|---|---|
| 400 | Arquivo ausente ou formato inválido |
| 422 | CSV com colunas incompatíveis |

---

## Tabela Resumo de Rotas

| Método | Rota | Autenticado? | Descrição |
|---|---|---|---|
| POST | `/auth/signup` | Não | Cria usuário |
| POST | `/auth/login` | Não | Retorna token JWT |
| POST | `/auth/refresh` | Não* | Renova token de acesso |
| POST | `/transacoes/classificar` | Sim | Classifica despesas automaticamente |
| POST | `/analise-financeira` | Sim | Análise completa + recomendações |
| GET | `/analises/historico` | Sim | Lista análises passadas do usuário |
| GET | `/analises/{id}` | Sim | Detalhe de uma análise específica |
| POST | `/transacoes/lote` | Sim | Upload CSV em lote |

*\* Usa o refresh token no corpo da requisição em vez do access token.*

---

## Observações de Implementação

- Como o edital sugere **Java + Spring Boot**, recomenda-se modelar desde já as entidades `Usuario`, `Transacao` e `AnaliseFinanceira`, já que o histórico e a autenticação dependem de persistência em banco de dados relacional.
- O modelo de Ciência de Dados (classificação de despesas e perfil financeiro) deve ser serializado (`.joblib` ou equivalente) e carregado pelo backend na inicialização ou sob demanda.
- Recomenda-se validação de entrada em todos os endpoints que recebem dados financeiros, com mensagens de erro estruturadas em JSON.
- A integração com OCI (Object Storage, Compute ou Functions) pode ser usada para armazenar os modelos serializados ou hospedar a aplicação. 