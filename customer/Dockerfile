FROM eclipse-temurin:21.0.8_9-jre-alpine
COPY target/customer*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]