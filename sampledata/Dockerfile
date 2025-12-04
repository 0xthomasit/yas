#FROM eclipse-temurin:21.0.8_9-jre-alpine
#COPY target/sampledata*.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# Stage 1: Use Maven for building
FROM maven:3.9.11-amazoncorretto-21-alpine AS build
WORKDIR /app

COPY ./common-library ./common-library
COPY ./sampledata ./sampledata

WORKDIR /app/common-library
RUN mvn clean install -DskipTests

WORKDIR /app/sampledata
RUN mvn clean package -DskipTests

# Staage 2: use Amazoncorretto for running
FROM amazoncorretto:21.0.9-alpine
RUN apk add --no-cache curl bash
WORKDIR /app
COPY --from=build /app/sampledata/target/*.jar sampledata.jar
ENTRYPOINT ["java","-jar","sampledata.jar"]