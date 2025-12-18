# ============================================
# Builder Stage - Build Spring Boot Application
# ============================================
FROM gradle:8.5-jdk17 AS builder

WORKDIR /build

# Copy Gradle configuration files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Copy source code
COPY src ./src

# Build the application (skip tests for faster build)
RUN gradle clean build -x test --no-daemon

# ============================================
# Runtime Stage - Run Application
# ============================================
FROM openjdk:17-jdk-slim

WORKDIR /app

# Install system dependencies
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        python3.11 \
        python3-pip \
        ffmpeg \
        imagemagick \
        fonts-nanum \
        && \
    # Clean up apt cache to reduce image size
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Create symlink for python command (some scripts may use 'python' instead of 'python3')
RUN ln -s /usr/bin/python3 /usr/bin/python

# Copy Python scripts
COPY scripts /app/scripts

# Install Python dependencies
RUN pip3 install --no-cache-dir -r /app/scripts/requirements.txt

# Copy the built JAR from builder stage
# Spring Boot creates both platform-*-SNAPSHOT.jar (boot JAR) and platform-*-SNAPSHOT-plain.jar
# We want the boot JAR (the one without -plain suffix)
COPY --from=builder /build/build/libs /tmp/libs
RUN find /tmp/libs -name "platform-*-SNAPSHOT.jar" ! -name "*-plain.jar" -exec mv {} /app/app.jar \; && \
    rm -rf /tmp/libs

# Expose application port
EXPOSE 8080

# Set entrypoint to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

