FROM openjdk:11-jre-slim
COPY target/near-miss-web-0.0.1-SNAPSHOT.jar /app/near-miss-web.jar
COPY src/main/resources/application-docker.properties /app/config/application-docker.properties
WORKDIR "/app"
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "near-miss-web.jar"]