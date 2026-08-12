# Plano de Endpoints da API

## Resumo dos Endpoints

| Método | URL | Autenticado | Descrição |
|---|---|---|---|
| POST | `/auth/signup` | Não | Cadastro de novo usuário |
| POST | `/auth/login` | Não | Autenticação e geração de token JWT |
| GET | `/usuarios/me` | Sim | Perfil do usuário autenticado |
| GET | `/usuarios/all` | Sim | Lista todos os usuários |
| GET | `/usuarios/{id}` | Sim | Busca usuário por ID |
| DELETE | `/usuarios/{id}` | Sim | Exclui usuário por ID |
| POST | `/transacoes` | Sim | Cria uma nova transação |
| GET | `/transacoes` | Sim | Lista transações do usuário autenticado |
| PUT | `/transacoes/{id}` | Sim | Atualiza transação por ID |
| DELETE | `/transacoes/{id}` | Sim | Exclui transação por ID |
| POST | `/transacoes/classificar` | Sim | Classifica a categoria de uma transação |
| POST | `/transacoes/lote` | Sim | Processamento de transações via arquivo CSV |
| POST | `/analise-financeira` | Sim | Gera análise financeira completa |
| GET | `/analise-financeira/historico` | Sim | Lista histórico de análises |
| GET | `/analise-financeira/{id}` | Sim | Detalhe de uma análise por ID |

---

## Formato Padrão de Erro (HTTP 4xx / 5xx)

```json
{
  "timestamp": "2026-07-21T17:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Mensagem de erro",
  "path": "/endpoint",
  "detalhes": [
    "A descrição não pode estar em branco."
  ]
}
```

---

## 1. Autenticação (`/auth`)

### `POST /auth/signup`
- **Autenticado:** Não
- **Entrada:**
```json
{
  "nome": "Maria Silva",
  "email": "maria.silva@email.com",
  "senha": "123456"
}
```
- **Saída (201 Created):**
```json
{
  "id": 1,
  "nome": "Maria Silva",
  "email": "maria.silva@email.com",
  "criado_em": "2026-07-20T14:30:00"
}
```

### `POST /auth/login`
- **Autenticado:** Não
- **Entrada:**
```json
{
  "email": "usuario@email.com",
  "senha": "123456"
}
```
- **Saída (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

---

## 2. Usuários (`/usuarios`)

### `GET /usuarios/me`
- **Autenticado:** Sim
- **Entrada:** Nenhuma (Body vazio)
- **Saída (200 OK):**
```json
{
  "id": 1,
  "nome": "Maria Silva",
  "email": "maria.silva@email.com",
  "criado_em": "2026-07-20T14:30:00"
}
```

### `GET /usuarios/all`
- **Autenticado:** Sim
- **Entrada:** Nenhuma (Body vazio)
- **Saída (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Maria Silva",
    "email": "maria.silva@email.com",
    "criado_em": "2026-07-20T14:30:00"
  }
]
```

### `GET /usuarios/{id}`
- **Autenticado:** Sim
- **Entrada:** Nenhuma (Body vazio)
- **Saída (200 OK):**
```json
{
  "id": 1,
  "nome": "Maria Silva",
  "email": "maria.silva@email.com",
  "criado_em": "2026-07-20T14:30:00"
}
```

### `DELETE /usuarios/{id}`
- **Autenticado:** Sim
- **Entrada:** Nenhuma (Body vazio)
- **Saída (204 No Content):** Nenhuma

---

## 3. Transações (`/transacoes`)

### `POST /transacoes`
- **Autenticado:** Sim
- **Entrada:**
```json
{
  "descricao": "Supermercado",
  "valor": 89.90
}
```
- **Saída (201 Created):**
```json
{
  "id": 1,
  "descricao": "Supermercado",
  "valor": 89.90,
  "categoria": "alimentacao",
  "usuario_id": 1,
  "criado_em": "2026-07-20T14:30:00"
}
```

### `GET /transacoes`
- **Autenticado:** Sim
- **Entrada:** Nenhuma (Body vazio)
- **Saída (200 OK):**
```json
[
  {
    "id": 1,
    "descricao": "Supermercado",
    "valor": 89.90,
    "categoria": "alimentacao",
    "usuario_id": 1,
    "criado_em": "2026-07-20T14:30:00"
  }
]
```

### `PUT /transacoes/{id}`
- **Autenticado:** Sim
- **Entrada:**
```json
{
  "descricao": "Supermercado Extra",
  "valor": 120.50
}
```
- **Saída (200 OK):**
```json
{
  "id": 1,
  "descricao": "Supermercado Extra",
  "valor": 120.50,
  "categoria": "alimentacao",
  "usuario_id": 1,
  "criado_em": "2026-07-20T14:30:00"
}
```

### `DELETE /transacoes/{id}`
- **Autenticado:** Sim
- **Entrada:** Nenhuma (Body vazio)
- **Saída (204 No Content):** Nenhuma

### `POST /transacoes/classificar`
- **Autenticado:** Sim
- **Entrada:**
```json
{
  "descricao": "Supermercado",
  "valor": 89.90
}
```
- **Saída (200 OK):**
```json
{
  "categoria": "alimentacao",
  "confianca": 0.94
}
```

### `POST /transacoes/lote`
- **Autenticado:** Sim
- **Entrada:** `multipart/form-data` (campo `arquivo`: arquivo `.csv` com colunas `descricao,valor`)
- **Saída (200 OK):**
```json
{
  "totalCriadas": 8,
  "totalFalhas": 2,
  "erros": [
    "Linha 3: Valor inválido"
  ]
}
```

---

## 4. Análise Financeira (`/analise-financeira`)

### `POST /analise-financeira`
- **Autenticado:** Sim
- **Entrada:**
```json
{
  "rendaMensal": 4500,
  "nivelEndividamento": 25,
  "frequenciaPoupanca": "Media"
}
```
- **Saída (200 OK):**
```json
{
  "perfil_financeiro": "Em observacao",
  "probabilidade": 0.82,
  "resumo_gastos": {
    "alimentacao": 420.00,
    "transporte": 300.00
  },
  "recomendacoes": [
    "Monitorar gastos de transporte",
    "Aumentar a reserva financeira"
  ]
}
```

### `GET /analise-financeira/historico`
- **Autenticado:** Sim
- **Entrada:** Nenhuma (Body vazio)
- **Saída (200 OK):**
```json
[
  {
    "id": 1,
    "criado_em": "2026-07-21T17:44:00",
    "renda_mensal": 4500,
    "nivel_endividamento": 25,
    "frequencia_poupanca": "Media",
    "perfil_financeiro": "Em observacao",
    "probabilidade": 0.75,
    "recomendacoes": [
      "Monitorar gastos de transporte"
    ]
  }
]
```

### `GET /analise-financeira/{id}`
- **Autenticado:** Sim
- **Entrada:** Nenhuma (Body vazio)
- **Saída (200 OK):**
```json
{
  "id": 1,
  "criado_em": "2026-07-21T17:44:00",
  "renda_mensal": 4500,
  "nivel_endividamento": 25,
  "frequencia_poupanca": "Media",
  "perfil_financeiro": "Em observacao",
  "probabilidade": 0.75,
  "recomendacoes": [
    "Monitorar gastos de transporte"
  ]
}
```