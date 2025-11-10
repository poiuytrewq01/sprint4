# Usa o Maven dentro do container
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o pom.xml e o código-fonte
COPY pom.xml .
COPY src ./src

# Compila o projeto (gera o .jar automaticamente)
RUN mvn clean package -DskipTests

# Segunda etapa: rodar o app
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o .jar gerado da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão
EXPOSE 8080

# Roda a aplicação
CMD ["java", "-jar", "app.jar"]
