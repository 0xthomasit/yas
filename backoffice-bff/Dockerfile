#FROM eclipse-temurin:21.0.8_9-jre-alpine
#RUN apk add --no-cache bash
#COPY target/backoffice*.jar app.jar
#COPY wait-for-it.sh wait-for-it.sh
#RUN chmod +x wait-for-it.sh
#ENTRYPOINT ["java","-jar","app.jar"]

# Stage 1: Use Maven for building
FROM maven:3.9.11-amazoncorretto-21-alpine AS build
WORKDIR /app

COPY ./backoffice-bff ./backoffice-bff
COPY ./backoffice-bff/wait-for-it.sh ./backoffice-bff/wait-for-it.sh

WORKDIR /app/backoffice-bff
RUN chmod +x wait-for-it.sh

# Stage 2: use Amazoncorretto for running
FROM amazoncorretto:21.0.9-alpine
RUN apk add --no-cache curl bash
WORKDIR /app
COPY --from=build /app/backoffice-bff/target/*.jar backoffice.jar
ENTRYPOINT ["java","-jar","backoffice.jar"]