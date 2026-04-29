# Estágio 1: Build da aplicação usando Maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia o pom.xml e baixa as dependências offline
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e faz o empacotamento
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Criação da imagem final otimizada apenas com o JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia apenas o JAR compilado do estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta que a aplicação roda
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
