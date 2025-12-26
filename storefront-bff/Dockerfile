# FROM eclipse-temurin:21-jre-alpine
# RUN apk add --no-cache bash
# COPY wait-for-it.sh wait-for-it.sh
# RUN chmod +x wait-for-it.sh
# COPY target/storefront-bff*.jar app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]

# Stage 1: Use Maven for building
FROM maven:3.9.12-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY ./pom.xml ./pom.xml
COPY ./storefront-bff/wait-for-it.sh ./storefront-bff/wait-for-it.sh
COPY ./storefront-bff ./storefront-bff

# Install parent POM first (required for dependency resolution)
RUN mvn install -N -DskipTests

WORKDIR /app/storefront-bff
RUN chmod +x wait-for-it.sh
# Build a jar file
RUN mvn clean package -DskipTests

# Stage 2: use eclipse-temurin for running
FROM eclipse-temurin:21.0.9_10-jre-alpine-3.23
RUN apk add --no-cache curl bash dos2unix
WORKDIR /app
COPY --from=build /app/storefront-bff/wait-for-it.sh wait-for-it.sh
RUN dos2unix wait-for-it.sh && chmod +x wait-for-it.sh
COPY --from=build /app/storefront-bff/target/*.jar storefront.jar
ENTRYPOINT ["java","-jar","storefront.jar"]