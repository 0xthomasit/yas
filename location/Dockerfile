# FROM eclipse-temurin:21-jre-alpine
# COPY target/location*.jar app.jar
# ENTRYPOINT ["java", "-jar", "/app.jar"]

# Stage 1: Use Maven for building
FROM maven:3.9.12-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY ./pom.xml ./pom.xml
COPY ./common-library ./common-library
COPY ./location ./location

# Install parent POM first (required for dependency resolution)
RUN mvn install -N -DskipTests

# Build and install common-library
WORKDIR /app/common-library
RUN mvn clean install -DskipTests

# Build location service
WORKDIR /app/location
RUN mvn clean package -DskipTests

# Stage 2: use eclipse-temurin for running
FROM eclipse-temurin:21.0.9_10-jre-alpine-3.23
RUN apk add --no-cache curl bash
WORKDIR /app
COPY --from=build /app/location/target/*.jar location.jar
ENTRYPOINT ["java","-jar","location.jar"]