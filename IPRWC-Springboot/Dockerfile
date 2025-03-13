# Stage 1: Build the application using Maven and JDK
FROM maven:3.8.5-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy only the pom.xml first, and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application, skipping tests to speed up the build
RUN mvn package -DskipTests

# Stage 2: Create a minimal image for running the application
FROM eclipse-temurin:17 AS runtime

WORKDIR /app

# Add a non-root user for security reasons
RUN useradd -u 1001 -r -s /bin/bash nonroot

# Copy the jar file from the build stage
COPY --from=builder /app/target/*.jar /app/app.jar

COPY src/main/resources/images /app/images


# Ensure the application runs as the non-root user
USER nonroot

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
