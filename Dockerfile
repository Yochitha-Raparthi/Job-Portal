
# Stage 1: Build Spring Boot using Java 25
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src ./src

# Build the application
RUN chmod +x mvnw && ./mvnw -B clean package -DskipTests

# Stage 2: Run Spring Boot using Java 25
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]
