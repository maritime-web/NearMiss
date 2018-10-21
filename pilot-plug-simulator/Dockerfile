FROM openjdk:11-jre-slim
COPY target/pilot-plug-simulator-0.0.1-SNAPSHOT.jar /app/pilot-plug-simulator.jar
WORKDIR "/app"
ENTRYPOINT ["java", "-jar", "pilot-plug-simulator.jar", "--port=9000", "--connect=gps-simulator:9898", "--connect=ais-simulator:9897"]
EXPOSE 9000/tcp
