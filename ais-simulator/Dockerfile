FROM openjdk:11-jre-slim
COPY target/ais-simulator-0.0.1-SNAPSHOT.jar /app/ais-simulator/ais-simulator.jar
COPY src/main/resources/application-docker.properties /app/ais-simulator/config/application-docker.properties
WORKDIR "/app/ais-simulator"
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "ais-simulator.jar"]
EXPOSE 9897/tcp