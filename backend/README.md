# ☕ Smart Finance - Backend (Spring Boot)

Este diretório contém a API REST do projeto **Smart Finance**, responsável pelo gerenciamento de dados de usuários e transações, orquestração da lógica de análise e comunicação com o serviço de Ciência de Dados.

---

## 🛠️ Tecnologias e Dependências

A aplicação foi configurada utilizando **Java 17** e **Spring Boot 3.3.1**. As principais dependências configuradas no [`pom.xml`](file:///c:/Users/Admin/Documents/projetos/G9-HACKATHON-TEST/backend/pom.xml) são:

*   **Spring Web**: Criação de endpoints RESTful.
*   **Spring Data JPA**: Abstração de banco de dados e persistência ORM.
*   **MySQL Connector/J**: Driver de conexão com o banco de dados MySQL.
*   **Spring Boot Starter Validation**: Validação sintática de payloads recebidos via Bean Validation.
*   **Lombok**: Geração automática de getters, setters, construtores e builders.
*   **Springdoc OpenAPI**: Geração automática de documentação Swagger UI.
*   **ModelMapper**: Facilita a conversão entre Entidades JPA e DTOs.

---

## 📁 Estrutura de Pacotes Planejada

A estrutura de código segue o padrão em camadas do ecossistema Spring:

```bash
com.hackathon.one/
├── FinanceApplication.java    # Classe principal de inicialização
├── controller/                # Exposição de endpoints REST (REST Controllers)
├── service/                   # Camada de lógica de negócio e regras do sistema
├── repository/                # Interfaces de comunicação com o MySQL (JPA Repositories)
├── model/                     # Entidades persistentes (Usuario, Transacao, etc.)
├── dto/                       # Objetos de Transferência de Dados (Requests/Responses)
├── exception/                 # Tratamento global de erros (@ControllerAdvice)
└── config/                    # Configurações do Spring (Swagger, ModelMapper, etc.)
```

---

## ⚙️ Configurações e Perfis (`application.yml`)

As configurações de ambiente devem ser divididas em perfis no arquivo `src/main/resources/application-dev.yml` (desenvolvimento local) e `application-docker.yml` (para execução em container).

**Configuração recomendada para `application-dev.yml`:**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/finance_db?useSSL=false&serverTimezone=UTC
    username: finance_user
    password: finance_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

---

## 🚀 Como Executar

1.  Garanta que o banco de dados MySQL esteja em execução:
    ```bash
    docker compose up -d
    ```
2.  Compile e inicie a aplicação Java:
    ```bash
    mvn clean spring-boot:run
    ```
3.  A documentação Swagger estará disponível em:
    [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)