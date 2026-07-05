# --- Build stage ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package -DskipTests

# --- Runtime stage ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN useradd -m appuser
COPY --from=build /build/target/document.search-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /app/data/indices && chown -R appuser:appuser /app
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
