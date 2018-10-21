FROM openjdk:11-jre-slim
COPY target/gps-simulator-0.0.1-SNAPSHOT-exec.jar /app/gps-simulator.jar
COPY src/main/resources/application-docker.properties /app/config/application-docker.properties
WORKDIR "/app"
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "gps-simulator.jar"]
EXPOSE 9898/tcp