#FROM eclipse-temurin:21.0.8_9-jre-alpine
#RUN apk add --no-cache bash
#COPY wait-for-it.sh wait-for-it.sh
#RUN chmod +x wait-for-it.sh
#COPY target/storefront*.jar app.jar
#ENTRYPOINT ["java","-jar","app.jar"]

# Stage 1: Use Maven for building
FROM maven:3.9.11-amazoncorretto-21-alpine AS build
WORKDIR /app

COPY ./storefront-bff ./storefront-bff
COPY ./storefront-bff/wait-for-it.sh ./storefront-bff/wait-for-it.sh

WORKDIR /app/storefront-bff
RUN chmod +x wait-for-it.sh

# Stage 2: use Amazoncorretto for running
FROM amazoncorretto:21.0.9-alpine
RUN apk add --no-cache curl bash
WORKDIR /app
COPY --from=build /app/storefront-bff/target/*.jar storefront.jar
ENTRYPOINT ["java","-jar","storefront.jar"]