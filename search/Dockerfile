# FROM eclipse-temurin:21.0.8_9-jre-alpine
# RUN apk add --no-cache bash
# COPY target/search*.jar app.jar
# COPY wait-for-it.sh wait-for-it.sh
# RUN chmod +x wait-for-it.sh
# ENTRYPOINT ["java", "-jar", "/app.jar"]

# Stage 1: Use Maven for building
FROM maven:3.9.11-amazoncorretto-21-alpine AS build
WORKDIR /app

COPY ./common-library ./common-library
COPY ./search ./search

WORKDIR /app/common-library
RUN mvn clean install -DskipTests

WORKDIR /app/search
RUN chmod +x wait-for-it.sh
RUN mvn clean package -DskipTests

# Stage 2: use Amazoncorretto for running
FROM amazoncorretto:21.0.9-alpine
RUN apk add --no-cache curl bash
WORKDIR /app
COPY --from=build /app/search/target/*.jar search.jar
COPY --from=build /app/search/wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh
ENTRYPOINT ["java","-jar","search.jar"]