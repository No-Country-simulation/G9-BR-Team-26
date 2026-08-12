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

## Pipeline de CI

O projeto tem integração contínua configurada em `.github/workflows/ci.yml`. A cada push ou Pull Request:
1. Compila o projeto e roda os testes JUnit
2. Sobe um MySQL temporário e roda o `smoke-test.ps1` completo

Resultado visível na aba "Actions" do repositório no GitHub.

## Infraestrutura OCI (Oracle Cloud Infrastructure)

### Status: Fases 1-4 concluídas (Compartment, Rede, Segurança, Object Storage)

### Recursos criados

| Recurso | Nome | Detalhes |
|---|---|---|
| Compartment | `finance-ai` | Organiza todos os recursos do projeto |
| VCN | `finance-ai-vcn` | `10.0.0.0/16`, região `sa-saopaulo-1` |
| Internet Gateway | `finance-ai-igw` | Acesso de/para internet |
| NAT Gateway | `finance-ai-natgw` | Saída de internet para subnet privada |
| Subnet pública | `finance-ai-subnet-public` | `10.0.1.0/24` — onde o Spring Boot vai rodar |
| Subnet privada | `finance-ai-subnet-private` | `10.0.2.0/24` — onde a API FastAPI vai rodar |
| Route Table pública | Default Route Table | Aponta para o Internet Gateway |
| Route Table privada | `finance-ai-rt-private` | Aponta para o NAT Gateway, associada à subnet privada |
| Bucket Object Storage | `finance-ai-modelos` | Armazena os modelos `.joblib` do Data Science |

### Portas liberadas (Security Lists)

Todas na Default Security List (associada às duas subnets):

| Porta | Origem permitida | Uso |
|---|---|---|
| 8080 | `0.0.0.0/0` (qualquer lugar) | Spring Boot — API pública |
| 22 | `0.0.0.0/0` | SSH — acesso administrativo |
| 8000 | `10.0.1.0/24` (só subnet pública) | FastAPI — acesso interno apenas, não exposto à internet |

### Configuração local — OCI CLI

O OCI CLI foi instalado e configurado localmente para gerenciar recursos via linha de comando.

**Credenciais:**
- Chave privada API: `oci-keys/oci_api_key.pem` (protegida no `.gitignore`, nunca commitada)
- Arquivo de configuração: `~/.oci/config` (fora do repositório, específico de cada máquina)

**Testar se está funcionando:**
```powershell
oci os ns get
```
Deve retornar o namespace da conta em JSON, sem erros.

### Problemas comuns enfrentados (já resolvidos)

| Sintoma | Causa | Solução |
|---|---|---|
| Erro ao criar segunda subnet: "DNS Label is a duplicate" | Nomes de subnet parecidos geram o mesmo DNS Label automático | Definir o DNS Label manualmente, diferente para cada subnet (ex: `subpublic`, `subprivate`) |
| Instalação do OCI CLI falha com erro de caminho de arquivo | Windows não tem suporte a "long paths" habilitado por padrão | Habilitar via registro (`LongPathsEnabled=1` em `HKLM:\SYSTEM\CurrentControlSet\Control\FileSystem`), como Administrador, e **reiniciar o computador** |
| `oci os ns get` não encontra o arquivo de config | Notepad salvou como `config.txt` em vez de `config` (extensão escondida) | Mesmo problema já visto com o `Dockerfile` — sempre confirmar com `dir` que o nome do arquivo não ganhou `.txt` sem querer |
| `WARNING: Permissions ... are too open` | Limitação conhecida do OCI CLI no Windows (o sistema de permissões dele foi pensado para Linux/Mac) | Cosmético, não afeta funcionamento. Silenciar com `$Env:OCI_CLI_SUPPRESS_FILE_PERMISSIONS_WARNING="True"` (vale só para a sessão atual) |
| `Move-Item`/`Rename-Item` não encontra o arquivo | Nome do arquivo baixado tinha espaço extra e extensão duplicada (`oci_api_key .pem.pem`) | Sempre conferir o nome exato com `dir` antes de tentar mover/renomear |

### Próximos passos (dependem do Data Science)

- Deploy da API FastAPI na subnet privada (bloqueado até o Data Science expor o serviço)
- Deploy do Spring Boot na subnet pública
- Integração entre os dois serviços pela rede interna da VCN

## Deploy na OCI (Oracle Cloud) — Backend

O backend está rodando em produção numa Compute Instance na OCI.

### Infraestrutura
- **Instância:** `finance-ai-spring-instance-v2` (shape `VM.Standard.A2.Flex`, Ampere/ARM, Always Free)
- **IP público:** `163.176.209.203`
- **Porta:** `8080`
- **Swagger:** `http://163.176.209.203:8080/swagger-ui/index.html`

### Como foi feito o deploy
1. Docker instalado na instância via `dnf` (repositório oficial do Docker, não o pacote `docker-engine` do Oracle Linux, que não existe)
2. Repositório clonado direto na instância via `git clone`
3. **Importante — arquitetura ARM:** o `Dockerfile` precisou ser ajustado, trocando `eclipse-temurin:17-jre-alpine` por `eclipse-temurin:17-jre` (a variante alpine não tem build para ARM)
4. Arquivo `.env` criado manualmente na instância (nunca commitado) com `DATASCIENCE_API_TOKEN`
5. `docker compose up -d --build` sobe MySQL + Spring Boot juntos

### Para atualizar o deploy (nova versão do código)
```bash
ssh -i oci-keys/finance-ai-ssh-key.key opc@163.176.209.203
cd G9-BR-Team-26/backend
git pull origin main
docker compose up -d --build
```