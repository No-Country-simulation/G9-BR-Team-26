# Setup do Projeto — Finance AI Backend

Guia para rodar o backend do zero, numa máquina limpa.

## Pré-requisitos

| Ferramenta | Versão | Observação |
|---|---|---|
| JDK | 17 (LTS) | **Obrigatório ser a 17.** Versões mais novas (ex: 25) causam falha de compilação silenciosa com o Lombok. |
| Maven | 3.9+ | |
| Docker Desktop | Qualquer versão recente | Precisa estar aberto e rodando antes dos próximos passos |
| Git | Qualquer versão recente | |

## Passo 1 — Clonar o repositório

```powershell
git clone https://github.com/No-Country-simulation/G9-BR-Team-26.git
cd G9-BR-Team-26
```

## Passo 2 — Confirmar o JDK

```powershell
java -version
```

Deve mostrar `17.x`. Se mostrar outra versão, ajuste o `JAVA_HOME` e o `PATH` antes de continuar (instalar o JDK 17 do Adoptium, se necessário).

## Passo 3 — Subir o banco de dados (MySQL via Docker)

O `docker-compose.yml` está na raiz do repositório.

```powershell
docker compose up -d
```

Confirme que subiu:
```powershell
docker ps
```
Deve aparecer um container `finance-mysql`, status `Up`.

**Credenciais do banco** (definidas no `docker-compose.yml` e usadas no `application.yml`):
- Usuário: `root`
- Senha: `root`
- Banco: `finance_db`
- Porta: `3306`

## Passo 4 — Rodar a aplicação Spring Boot

```powershell
cd backend
mvn spring-boot:run
```

Aguarde aparecer no log:Isso pode demorar mais na primeira vez (o Maven baixa dependências) — até ~90 segundos é normal.

## Passo 5 — Confirmar que está tudo funcionando

Com a aplicação rodando (não feche esse terminal), abra **outro terminal** e rode:

```powershell
cd C:\Projetos\G9-BR-Team-26\scripts
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\smoke-test.ps1
```

Resultado esperado: `Todos os testes passaram!` (11 de 11).

## Estrutura do projeto                                                                  G9-BR-Team-26/
├── backend/ # API Spring Boot (Java 17, Spring Security, JPA, MySQL)
├── data-science/ # Pipeline de ML e API FastAPI
├── docker/ # Documentação de infraestrutura
├── docs/ # Planejamento (alguns arquivos desatualizados — ver nota abaixo)
├── scripts/ # smoke-test.ps1 — validação rápida da aplicação
├── docker-compose.yml # Sobe o MySQL
└── SETUP.md # Este arquivo                                                                                                                                                                  
## Problemas comuns (já enfrentados e resolvidos durante o desenvolvimento)

| Sintoma | Causa | Solução |
|---|---|---|
| `cannot find symbol` em métodos `getX()`/`setX()` ao compilar | JDK incompatível com o Lombok (ex: JDK 25) | Trocar para JDK 17 |
| `Access denied for user 'root'` ao subir a aplicação | Senha do MySQL no `docker-compose.yml` não bate com o `application.yml` | Confirmar que ambos usam `root`/`root`. Se mudou o `docker-compose.yml` depois de já ter subido o container uma vez, rodar `docker compose down -v` e subir de novo (isso apaga os dados existentes) |
| `Web server failed to start. Port 8080 was already in use` | Uma instância anterior da aplicação ainda está rodando | Fechar o terminal antigo, ou `netstat -ano \| findstr :8080` + `taskkill /PID <pid> /F` |
| `No plugin found for prefix 'X'` ao rodar `mvn` | Comando digitado errado (ex: `srping-boot` em vez de `spring-boot`) | Conferir o comando exato: `mvn spring-boot:run` |
| Script `.ps1` não executa, erro de política de segurança | PowerShell bloqueia scripts por padrão | `Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass` (vale só para a sessão atual do terminal) |

## Notas sobre documentação do repositório

- `docs/PLANO-ENDPOINTS.md` está **desatualizado** (categorias antigas, formato de resposta diferente do implementado) — confirmado obsoleto pelo time em 03/08. Não usar como referência.
- A fonte de verdade atual para contratos de API é o `README.md` da raiz do repositório e o `data-science/README.md`.