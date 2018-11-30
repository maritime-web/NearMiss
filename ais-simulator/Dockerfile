FROM openjdk:11-jre-slim
COPY target/ais-simulator-0.0.1-SNAPSHOT.jar /app/ais-simulator.jar
COPY src/main/resources/application-docker.properties /app/config/application-docker.properties
COPY src/main/resources/csv/test.csv /app/csv/test.csv
COPY src/main/resources/csv/test.csv /app/csv/1169__resund77261169-20181119-0.csv
WORKDIR "/app"
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "ais-simulator.jar"]
EXPOSE 9897/tcp