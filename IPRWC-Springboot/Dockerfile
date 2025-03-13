# Stage 1: Build the application using Maven and JDK
FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal image for running the application
FROM eclipse-temurin:17 AS runtime

WORKDIR /app

# Add a non-root user for security reasons
RUN useradd -u 1001 -r -s /bin/bash nonroot

# Copy the jar file from the build stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Ensure the application runs as the non-root user
USER nonroot

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
