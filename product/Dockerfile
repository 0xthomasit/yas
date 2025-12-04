# Stage 1: Use Maven for building
FROM maven:3.9.11-amazoncorretto-21-alpine AS build
# RUN apk add --no-cache curl bash
WORKDIR /app

COPY ./common-library ./common-library
COPY ./product ./product

WORKDIR /app/common-library
RUN mvn clean install -DskipTests

WORKDIR /app/product
RUN mvn clean package -DskipTests

# Staage 2: use Amazoncorretto for running
FROM amazoncorretto:21.0.9-alpine
RUN apk add --no-cache curl bash
WORKDIR /app
COPY --from=build /app/product/target/*.jar product.jar
ENTRYPOINT ["java","-jar","product.jar"]

#FROM eclipse-temurin:21.0.8_9-jre-alpine
#COPY target/product*.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]