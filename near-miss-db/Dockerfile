FROM openjdk:11-jre-slim
COPY target/near-miss-db-0.0.1-SNAPSHOT-exec.jar /app/near-miss-db.jar
COPY src/main/resources/application-docker.properties /app/config/application-docker.properties
WORKDIR "/app"
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "near-miss-db.jar"]
EXPOSE 8081/tcp