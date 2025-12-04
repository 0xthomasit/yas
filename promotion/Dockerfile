#FROM eclipse-temurin:21.0.8_9-jre-alpine
#COPY target/promotion*.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# Stage 1: Use Maven for building
FROM maven:3.9.11-amazoncorretto-21-alpine AS build
WORKDIR /app

COPY ./common-library ./common-library
COPY ./promotion ./promotion

WORKDIR /app/common-library
RUN mvn clean install -DskipTests

WORKDIR /app/promotion
RUN mvn clean package -DskipTests

# Staage 2: use Amazoncorretto for running
FROM amazoncorretto:21.0.9-alpine
RUN apk add --no-cache curl bash
WORKDIR /app
COPY --from=build /app/promotion/target/*.jar promotion.jar
ENTRYPOINT ["java","-jar","promotion.jar"]