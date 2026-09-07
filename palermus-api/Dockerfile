# Etapa 1: Construir la aplicación
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar el código fuente y el pom.xml
COPY pom.xml .
COPY src ./src

# Compilar el proyecto usando la imagen de Maven (evita problemas de formato CRLF en Windows con mvnw)
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la aplicación
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto por defecto de Spring Boot
EXPOSE 8080

# Usar la variable de entorno PORT si es proporcionada (recomendado para Google Cloud Run)
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]