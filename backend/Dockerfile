# ═══════════════════════════════════════════════════════════
#  Dockerfile — Finance AI Backend (Spring Boot)
#  Build em duas etapas: primeiro compila com Maven,
#  depois roda só o .jar final numa imagem menor.
# ═══════════════════════════════════════════════════════════

FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/finance-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]