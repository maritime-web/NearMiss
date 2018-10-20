# NearMiss

To run the full application locally do following:

1. Start gps-simulator
`java -jar gps-simulator-0.0.1-SNAPSHOT-exec.jar`

2. Start ais-simulator
`java -jar ais-simulator-0.0.1-SNAPSHOT.jar`   

3. Start pilot-plug-simulator using parameters
`java -jar pilot-plug-simulator-0.0.1-SNAPSHOT.jar --port=9000 --connectTo=localhost:9898 --connectTo=loalhost:9897`
Refer to README.adoc in the sub project.

4. Start near-miss-db using the h2tcp profile starting the h2 mem db in tcp mode.
`java -jar near-miss-db-0.0.1-SNAPSHOT-exec.jar --spring.profiles.active=h2tcp --spring.config.location=../src/main/resources`

5. Start near-miss-engine using h2tcp profile.
`java -jar near-miss-engine-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2tcp --spring.config.location=../src/main/resources/`

6. Start near-miss-web using h2tcp profile.
`java -jar near-miss-web-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2tcp --spring.config.location=../src/main/resources`



