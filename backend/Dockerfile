# ═══════════════════════════════════════════════════════════
#  Dockerfile — Finance AI Backend (Spring Boot)
#  Build em duas etapas: primeiro compila com Maven,
#  depois roda só o .jar final numa imagem menor.
# ═══════════════════════════════════════════════════════════

# ── Etapa 1: build ────────────────────────────────────────
# Usa uma imagem com Maven + JDK 17 só para compilar.
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copia primeiro só o pom.xml (para o Docker aproveitar cache
# de dependências, se o código mudar mas as dependências não).
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Agora copia o código-fonte e compila de fato.
COPY src ./src
RUN mvn clean package -DskipTests

# ── Etapa 2: execução ─────────────────────────────────────
# Imagem final, bem mais leve — só o Java Runtime (não o Maven,
# não o código-fonte, só o necessário para RODAR o .jar já pronto.
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia só o .jar final gerado na etapa de build.
COPY --from=build /app/target/finance-0.0.1-SNAPSHOT.jar app.jar

# Porta que a aplicação usa (documentação, não abre a porta sozinha).
EXPOSE 8080

# Comando que roda quando o container inicia.
ENTRYPOINT ["java", "-jar", "app.jar"]