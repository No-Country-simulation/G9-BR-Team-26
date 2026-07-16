# 🐳 Smart Finance - Infraestrutura e Docker Compose

`Futuramente as dockerfiles da api e da data-science estarão aquipara facilitar o deploy.`

Este diretório contém documentações e scripts auxiliares para a execução de serviços containerizados do projeto. O serviço de banco de dados é orquestrado através do arquivo principal [`docker-compose.yml`](file:///c:/Users/Admin/Documents/projetos/G9-HACKATHON-TEST/docker-compose.yml) na raiz do projeto.

---

## 🛢️ Serviço MySQL

Utilizamos a imagem oficial do **MySQL 8.0** para persistência de dados do MVP.

### Credenciais e Variáveis de Ambiente
*   **Imagem**: `mysql:8.0`
*   **Porta Exposta**: `3306`
*   **Banco de Dados Padrão**: `finance_db`
*   **Usuário**: `finance_user`
*   **Senha do Usuário**: `finance_password`
*   **Senha de Root**: `rootpassword`

---

## 🛠️ Comandos Úteis

### Inicializar os Serviços
Para subir o banco de dados em segundo plano (modo detached), execute a partir da raiz:
```bash
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
Os dados do banco de dados são mantidos de forma persistente através do volume nomeado `mysql_data`, gerenciado localmente pelo Docker Engine. Isso garante que a exclusão/recriação dos containers não cause a perda do banco de dados configurado pelo Spring Boot.