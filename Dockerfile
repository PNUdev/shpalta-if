FROM openjdk:11-jre-slim

COPY /radio-service-api/target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]