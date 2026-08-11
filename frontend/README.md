# SmartFinance - Sistema de Gestão e Análise Financeira

**SmartFinance** é uma plataforma web moderna de gestão e análise preditiva financeira desenvolvida com **React**, **TypeScript** e **Tailwind CSS**, com apoio da **Oracle** e **NoCountry**.

O sistema oferece controle de movimentações financeiras, simulação preditiva de risco e recomendação inteligente para saúde financeira do usuário.

---

## 🛠️ Tecnologia e Arquitetura Frontend

A aplicação foi construída seguindo os mais elevados padrões de desenvolvimento frontend, focando em performance, acessibilidade e modularidade.

### Tech Stack:
- **Core Framework:** React 18 (Vite)
- **Linguagem:** TypeScript (Tipagem estática completa)
- **Estilização:** Tailwind CSS (Arquitetura responsiva e suporte nativo a Tema Claro/Escuro)
- **Ícones:** Lucide React
- **Roteamento:** React Router DOM (v6) com controle avançado de Query Params
- **Visualização de Dados:** Lucide, Recharts / D3.js para gráficos dinâmicos

---

## 🎨 Design System e Interface

1. **Tema de Fundo Limpo (Full White Light / Deep Dark):**
   - No modo claro, a interface utiliza um fundo limpo e brilhante (`bg-white`), destacando cards e dados com contraste WCAG AA.
   - Suporte nativo a **Modo Escuro (Dark Mode)** sincronizado via `ThemeContext`.

2. **Navegação Inteligente (Sidebar & Header):**
   - Destaque ativo de rota ajustado com `useLocation` e parâmetros de busca (`?tab=import`, `?new=true`), garantindo que o item selecionado na Navbar corresponda exatamente à aba em exibição.
   - Header com busca global de transações e menu de atalhos rápidos.

3. **Inclusão e Sem Foto de Estoque:**
   - Ícone de perfil limpo e vetorial em SVG (`<User />`), sem dependência de imagens externas ou ilustrações não profissionais.

4. **Footer Institucional:**
   - Rodapé oficial do projeto: **SmartFinance • Com apoio Oracle X NoCountry** com indicação de certificado SSL 256-bit.

---

## 📁 Estrutura de Pastas

```text
src/
├── components/
│   ├── common/           # Componentes reutilizáveis (Button, Card, Modal, Input, Badge, Table)
│   └── layout/           # Componentes de estrutura (DashboardLayout, Header, Sidebar, Footer)
├── constants/            # Constantes de rotas, categorias e metadados
├── contexts/             # Provedores de contexto (ThemeContext, AuthContext)
├── hooks/                # Custom React Hooks para lógica de negócio e persistência
├── pages/
│   ├── Dashboard/        # Visão geral de KPIs e distribuição de receitas/despesas
│   ├── Transactions/     # Tabela de transações e módulo de importação CSV
│   ├── FinancialAnalysis/# Modelo preditivo e análise de crédito com IA
│   ├── Reports/          # Histórico de análises e exportação de dados
│   └── Settings/         # Perfil do usuário e preferências do sistema
├── types/                # Definições de interfaces TypeScript (Transaction, Analysis, User)
└── utils/                # Utilitários de formatação de moeda (R$) e datas
```

---

## 🚀 Funcionalidades Principais

### 1. Dashboard (`/dashboard`)
- Indicadores chave de desempenho (Saldo Total, Entradas, Saídas, Investimentos).
- Gráficos visuais de categorias e fluxo mensal.
- Atalho para registro de novas movimentações.

### 2. Transações (`/transactions`)
- **Gestão de Transações:** Listagem completa com filtros por pesquisa, categoria e tipo.
- **Operações CRUD:** Criação, edição e exclusão de transações em tempo real.
- **Importação CSV (`/transactions?tab=import`):** Upload de arquivos `.csv` com prévia de formato e validação de colunas (`descricao`, `valor`).

### 3. Análise Financeira (`/analysis`)
- Simulação preditiva de capacidade de pagamento e margem de segurança.
- Classificação do perfil financeiro (*Conservador*, *Moderado*, *Arrojado*).
- Recomendações acionáveis para otimização de orçamento e reserva de emergência.

### 4. Histórico de Análises (`/reports`)
- Registro cronológico de simulações anteriores com barras de progresso de probabilidade.
- Modal de detalhamento completo por registro.
- **Exportação de Relatórios:** Download de relatórios nos formatos **Excel (.xlsx)** e **CSV**.

### 5. Perfil e Configurações (`/settings`)
- Edição de informações pessoais (Nome, E-mail, Cargo).
- Visualização de detalhes da conta e ID exclusivo.
- Alternância instantânea de tema (Modo Claro vs. Modo Escuro).

---

## 💻 Como Executar o Projeto Localmente

1. **Clonar e acessar o repositório:**
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd <NOME_DA_PASTA>
   ```

2. **Instalar as dependências:**
   ```bash
   npm install
   ```

3. **Configurar as variáveis de ambiente:**
   Crie um arquivo `.env` na raiz do projeto (ou copie o `.env.example`):
   ```bash
   cp .env.example .env
   ```
   Adicione sua chave da API do Gemini no arquivo `.env`:
   ```env
   GEMINI_API_KEY=sua_chave_gemini_aqui
   ```

4. **Executar o ambiente de desenvolvimento:**
   ```bash
   npm run dev
   ```
   Acesse a aplicação no seu navegador em: `http://localhost:3000`

5. **Outros comandos disponíveis:**
   ```bash
   # Verificar erros de código e tipos TypeScript
   npm run lint

   # Gerar o build de produção (Full-Stack Express + Vite)
   npm run build

   # Executar a versão de produção compilada
   npm start
   ```

---

## 🏢 Créditos e Apoio

Desenvolvido por **SmartFinance** em parceria e apoio com **Oracle** X **NoCountry**.
