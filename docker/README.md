# 🐳 Smart Finance - Infraestrutura e Docker Compose

`Futuramente as dockerfiles da api e da data-science estarão aqui para facilitar o deploy.`

Este diretório contém documentações e scripts auxiliares para a execução de serviços containerizados do projeto. Os serviços de banco de dados e backend são orquestrados através do arquivo [`backend/docker-compose.yml`](../backend/docker-compose.yml), dentro da pasta `backend/` (não na raiz do projeto).

---

## 🛢️ Serviço MySQL

Utilizamos a imagem oficial do **MySQL 8.0** para persistência de dados do MVP.

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

---

## 🛠️ Comandos Úteis

### Inicializar os Serviços
Para subir o banco de dados e o backend em segundo plano (modo detached), execute a partir de `backend/` (com um arquivo `.env` configurado, ver `.env.example`):
```bash
cd backend
docker compose up -d
```

### Verificar o Status dos Containers
```bash
docker compose ps
```

### Visualizar Logs do Banco de Dados
```bash
docker compose logs -f mysql
```

### Encerrar os Serviços
Para parar os containers sem remover os volumes de dados persistidos:
```bash
docker compose down
```

Para remover os containers e **excluir permanentemente** todos os dados salvos:
```bash
docker compose down -v
```

---

## 💾 Persistência de Dados
Os dados do banco de dados são mantidos de forma persistente através do volume nomeado `mysql-data` (com hífen), gerenciado localmente pelo Docker Engine. Isso garante que a exclusão/recriação dos containers não cause a perda do banco de dados configurado pelo Spring Boot.