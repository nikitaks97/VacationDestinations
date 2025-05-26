# Multi-stage build for Spring Boot Application

# Stage 1: Build stage
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /app

# Copy Maven configuration
COPY pom.xml ./
COPY .mvn ./.mvn
COPY mvnw ./

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Development stage (optional, for development with hot reload)
FROM builder AS development
EXPOSE 8080 35729
CMD ["./mvnw", "spring-boot:run"]

# Stage 3: Production runtime stage
FROM eclipse-temurin:21-jre-alpine AS production

# Install curl for health checks and create application user
RUN apk add --no-cache curl && \
    addgroup --system spring && \
    adduser --system spring --ingroup spring

# Set working directory
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to spring user
RUN chown spring:spring app.jar

# Create the data directory and set ownership
RUN mkdir -p /app/data && chown -R spring:spring /app/data app.jar

# Switch to non-root user
USER spring:spring

# Health check configuration
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD ["sh", "/app/healthcheck.sh"]

# Expose application port
EXPOSE 8080

# JVM optimization arguments for containerized environments
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+UseStringDeduplication \
               -XX:+OptimizeStringConcat \
               -XX:+UseCompressedOops \
               -Djava.security.egd=file:/dev/./urandom"

# Application startup command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

# Default stage is production
FROM production
