# Docker e infraestrutura

Este diretório centraliza a documentação de contêineres. Os arquivos de build ficam ao lado de cada serviço para que cada imagem tenha o contexto de build correto.

## Por que existem dois Dockerfiles?

Há dois serviços independentes, com runtimes e dependências diferentes. Por isso cada um precisa da sua própria imagem:

| Arquivo | Serviço | Base | Motivo |
|---|---|---|---|
| [`backend/Dockerfile`](../backend/Dockerfile) | API Java Spring Boot | Maven 3.9 + Eclipse Temurin 17 | Compila o JAR em uma etapa e executa somente o artefato final em uma imagem JRE. |
| [`data-science/Dockerfile`](../data-science/Dockerfile) | API de ciência de dados FastAPI | Python 3.11 slim | Instala dependências Python, inclui modelos/artefatos e inicia o Uvicorn. |

Os Dockerfiles **não são duplicados**: o primeiro produz a API de regras de negócio e o segundo produz o serviço de inferência de ML. Eles podem ser implantados, escalados e atualizados separadamente. O backend se comunica com o Data Science por HTTP usando `DATASCIENCE_API_URL`.

## Compose atual: backend + MySQL

O arquivo [`backend/docker-compose.yml`](../backend/docker-compose.yml) sobe:

- `mysql`: MySQL 8, com volume nomeado `mysql-data` e porta `3306` exposta;
- `backend`: API Spring Boot construída a partir de `backend/Dockerfile`, exposta na porta `8080`.

O Data Science ainda não está declarado nesse Compose; execute-o localmente ou construa sua imagem separadamente conforme a seção seguinte.

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

- Arquivos `.env` contêm segredos e ficam ignorados pelo Git.
- Não registre nem publique `GEMINI_API_KEY`, tokens ou credenciais reais.
- Em produção, prefira variáveis providas pelo orquestrador/gerenciador de segredos em vez de copiar arquivos `.env`.
