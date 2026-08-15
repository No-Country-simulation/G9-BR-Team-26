# Docker e infraestrutura

`Futuramente as dockerfiles da api e da data-science estarão aqui para facilitar o deploy.`

Este diretório contém documentações e scripts auxiliares para a execução de serviços containerizados do projeto. Os serviços de banco de dados e backend são orquestrados através do arquivo [`backend/docker-compose.yml`](../backend/docker-compose.yml), dentro da pasta `backend/` (não na raiz do projeto).

Há dois serviços independentes, com runtimes e dependências diferentes. Por isso cada um precisa da sua própria imagem:

| Arquivo | Serviço | Base | Motivo |
|---|---|---|---|
| [`backend/Dockerfile`](../backend/Dockerfile) | API Java Spring Boot | Maven 3.9 + Eclipse Temurin 17 | Compila o JAR em uma etapa e executa somente o artefato final em uma imagem JRE. |
| [`data-science/Dockerfile`](../data-science/Dockerfile) | API de ciência de dados FastAPI | Python 3.11 slim | Instala dependências Python, inclui modelos/artefatos e inicia o Uvicorn. |

Os Dockerfiles **não são duplicados**: o primeiro produz a API de regras de negócio e o segundo produz o serviço de inferência de ML. Eles podem ser implantados, escalados e atualizados separadamente. O backend se comunica com o Data Science por HTTP usando `DATASCIENCE_API_URL`.

### Credenciais e Variáveis de Ambiente
Nenhuma credencial fica hardcoded no `docker-compose.yml` — todas vêm de variáveis de
ambiente, lidas de um arquivo `.env` (não versionado) na pasta `backend/`. Veja
`backend/.env.example` para a lista completa de chaves esperadas.

*   **Imagem**: `mysql:8.0`
*   **Porta**: `3306`, exposta somente na rede interna do Docker (`expose`) — não é
    publicada no host, para reduzir a superfície de ataque.
*   **Banco de Dados Padrão**: `finance_db`
*   **Usuário da aplicação**: definido por `MYSQL_USER` / `MYSQL_PASSWORD`
*   **Senha de Root**: definida por `MYSQL_ROOT_PASSWORD`

O arquivo [`backend/docker-compose.yml`](../backend/docker-compose.yml) sobe:

- `mysql`: MySQL 8, com volume nomeado `mysql-data` e porta `3306` exposta;
- `backend`: API Spring Boot construída a partir de `backend/Dockerfile`, exposta na porta `8080`.

### Inicializar os Serviços
Para subir o banco de dados e o backend em segundo plano (modo detached), execute a partir de `backend/` (com um arquivo `.env` configurado, ver `.env.example`):
```bash
cd backend
docker compose up -d
```

### Configuração

Antes de subir o backend, crie `backend/.env`:

```env
GEMINI_API_KEY=sua_chave_gemini
DATASCIENCE_API_URL=http://host.docker.internal:7070
DATASCIENCE_API_TOKEN=token-nao-configurado
```

`GEMINI_API_KEY` é consumida exclusivamente pelo backend. Não a coloque em arquivos `.env` do frontend nem a exponha com prefixo `VITE_`.

### Comandos

```bash
cd backend

# Construir e subir backend + MySQL
docker compose up -d --build

# Acompanhar os logs
docker compose logs -f backend

# Ver status
docker compose ps

# Parar, preservando os dados do MySQL
docker compose down

# Parar e remover o volume do banco (ação destrutiva)
docker compose down -v
```

API e Swagger estarão disponíveis, respectivamente, em `http://localhost:8080` e `http://localhost:8080/swagger-ui/index.html`.

## Imagem do Data Science

Para construir e executar apenas a API FastAPI:

```bash
docker build -t smartfinance-data-science ./data-science
docker run --rm -p 7070:7070 smartfinance-data-science
```

O Dockerfile do Data Science expõe a porta `7070`. Ao usar essa imagem junto ao backend em contêiner, defina `DATASCIENCE_API_URL` com um endereço alcançável pelo contêiner do backend (por exemplo, um nome de serviço de um Compose unificado).

## Observações de segurança

## 💾 Persistência de Dados
Os dados do banco de dados são mantidos de forma persistente através do volume nomeado `mysql-data` (com hífen), gerenciado localmente pelo Docker Engine. Isso garante que a exclusão/recriação dos containers não cause a perda do banco de dados configurado pelo Spring Boot.
