# Base image with Java 21
FROM eclipse-temurin:21-jdk-jammy

# Create a non-root user matching host UID
ARG APP_UID=1000
RUN useradd -u ${APP_UID} -ms /bin/bash mycar

# Switch to non-root user
USER mycar

# Copy the JAR built by CI/CD
COPY mycar.jar app.jar

EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]