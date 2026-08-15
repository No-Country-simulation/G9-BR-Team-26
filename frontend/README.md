# FinanceAI — Frontend

Cliente web do FinanceAI, construído com React, TypeScript, Vite e Tailwind CSS. Ele consome a API Spring Boot; não acessa diretamente MySQL, FastAPI ou Gemini.

## Tecnologias

- React 19 e TypeScript
- Vite 6
- Tailwind CSS 4
- React Router
- TanStack Query
- Axios
- Lucide React e Recharts

## Funcionalidades

- Autenticação e persistência de sessão JWT.
- Dashboard, transações, importação CSV e metas financeiras.
- Análise financeira, relatórios e simulação de quitação.
- Perfil, preferências de tema e rotas protegidas.
- Chat Fai (`/ia`), que apresenta um estado de indisponibilidade e bloqueia novos envios caso o endpoint falhe.

## Pré-requisitos

- Node.js 20+ (LTS recomendado)
- npm
- Backend em execução, por padrão em `http://localhost:8080`

## Configuração

Copie o arquivo de exemplo:

```bash
cp .env.example .env
```

No Windows PowerShell, caso o `cp` não esteja disponível:

```powershell
Copy-Item .env.example .env
```

Variáveis suportadas:

```env
VITE_API_BASE_URL=http://localhost:8080
```

`VITE_API_BASE_URL` é opcional: sem ela, a aplicação usa `http://localhost:8080`.

> Não defina `GEMINI_API_KEY` neste diretório. A chave pertence ao backend, em `backend/.env`, pois só o servidor chama a API Gemini.

## Executar localmente

```bash
npm install
npm run dev
```

Abra `http://localhost:3000`.

## Scripts

| Comando | Descrição |
|---|---|
| `npm run dev` | Inicia o servidor Vite na porta 3000. |
| `npm run lint` | Executa a verificação de tipos com TypeScript. |
| `npm run build` | Gera o build de produção e o bundle do servidor. |
| `npm start` | Inicia o servidor gerado em `dist/`. |

## Estrutura principal

```text
src/
├── components/     # Componentes comuns e de layout
├── constants/      # Rotas, categorias e constantes
├── contexts/       # AuthContext e ThemeContext
├── hooks/          # Consultas e mutações da API
├── pages/          # Telas da aplicação, incluindo AI/AIPage.tsx
├── services/       # Cliente Axios, sessão e repositórios
├── types/          # Tipos TypeScript
└── utils/          # Formatação e funções utilitárias
```

## Integração com a API e tratamento de erros

O cliente Axios centralizado fica em `src/services/api/axios.ts`. Ele:

- adiciona o token JWT às rotas protegidas;
- normaliza erros de rede e respostas da API;
- invalida a sessão em respostas `401` ou `403`.

O tratamento é isolado por tela. Em particular, quando `/fai/chat` retorna timeout, erro de rede, `5xx` ou erro de configuração do Gemini, a tela da Fai exibe “Funcionalidade indisponível no momento. Tente novamente mais tarde.” e desabilita o formulário de mensagens. As demais telas continuam operando normalmente.

## Produção

Defina `VITE_API_BASE_URL` durante o build para apontar ao endereço público do backend. Como variáveis `VITE_*` são embutidas no bundle, nunca use esse prefixo para segredos.
