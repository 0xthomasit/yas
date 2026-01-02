# FROM eclipse-temurin:21-jre-alpine
# RUN apk add --no-cache bash
# COPY target/backoffice*.jar app.jar
# COPY wait-for-it.sh wait-for-it.sh
# RUN chmod +x wait-for-it.sh
# ENTRYPOINT ["java","-jar","/app.jar"]

# Stage 1: Use Maven for building
FROM maven:3.9.12-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY ./pom.xml ./pom.xml
COPY ./backoffice-bff/wait-for-it.sh ./backoffice-bff/wait-for-it.sh
COPY ./backoffice-bff ./backoffice-bff

# Install parent POM first (required for dependency resolution)
RUN mvn install -N -DskipTests

WORKDIR /app/backoffice-bff
RUN chmod +x wait-for-it.sh
# Build a jar file
RUN mvn clean package -DskipTests

# Stage 2: use eclipse-temurin for running
FROM eclipse-temurin:21.0.9_10-jre-alpine-3.23
RUN apk add --no-cache curl bash dos2unix
WORKDIR /app
COPY --from=build /app/backoffice-bff/wait-for-it.sh wait-for-it.sh
RUN dos2unix wait-for-it.sh && chmod +x wait-for-it.sh
COPY --from=build /app/backoffice-bff/target/*.jar backoffice-bff.jar
ENTRYPOINT ["java","-jar","backoffice-bff.jar"]