# FROM eclipse-temurin:21-jre-alpine
# RUN apk add --no-cache bash
# COPY target/search*.jar app.jar
# COPY wait-for-it.sh wait-for-it.sh
# RUN chmod +x wait-for-it.sh
# ENTRYPOINT ["java", "-jar", "/app.jar"]

# Stage 1: Use Maven for building
FROM maven:3.9.12-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY ./pom.xml ./pom.xml
COPY ./common-library ./common-library
COPY ./search/wait-for-it.sh ./search/wait-for-it.sh
COPY ./search ./search

# Install parent POM first (required for dependency resolution)
RUN mvn install -N -DskipTests

# Build and install common-library
WORKDIR /app/common-library
RUN mvn clean install -DskipTests

# Build search service
WORKDIR /app/search
RUN chmod +x wait-for-it.sh
# Build a jar file
RUN mvn clean package -DskipTests

# Stage 2: use eclipse-temurin for running
FROM eclipse-temurin:21.0.9_10-jre-alpine-3.23
RUN apk add --no-cache curl bash dos2unix
WORKDIR /app
COPY --from=build /app/search/wait-for-it.sh wait-for-it.sh
RUN dos2unix wait-for-it.sh && chmod +x wait-for-it.sh
COPY --from=build /app/search/target/*.jar search.jar
ENTRYPOINT ["java","-jar","search.jar"]