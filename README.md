# NearMiss


## Building the Application
Use Maven: 
`mvn clean install`

This can be done form the application root folder og from withing the individual module folders.

The modules using db will use the in memory H2 database during unit testing, like when running:
`mvn test`

Spring default profile (application.properties) will be used when running the tests.

## Running the Application
The application can be executed in Docker containers or on the local machine 
(form command line or in a development tool).

### Running the Application in Docker Containers
When running the application through Docker a mySql database is used (running in a separate Docker container).

#### Docker Compose 
Use docker-compose through below given command lines to  build, start and stop the application.

Building the Docker images:
`docker-compose build`
<br>Starting the Application:
`docker-compose up`
<br>Stopping the Application:
`docker-compose down`

The docker profile (application-docker.properties) will be used when running Docker.

### Running Individual Modules
If the application is to be started through individual Docker commands.
<br>Please refer to the `docker-compose.yml` file for 
dependencies between the individual modules of the application.


### Running the Application locally
When the application is running locally a in memory H2 databse is used in server mode (TCP).

To run the full application locally do following:

1. Start gps-simulator
<br>`java -jar gps-simulator-0.0.1-SNAPSHOT-exec.jar`

2. Start ais-simulator
<br>`java -jar ais-simulator-0.0.1-SNAPSHOT.jar`   

3. Start pilot-plug-simulator using parameters
<br>`java -jar pilot-plug-simulator-0.0.1-SNAPSHOT.jar --port=9000 --connectTo=localhost:9898 --connectTo=loalhost:9897`
<br>Refer to README.adoc in the sub project.

4. Start near-miss-db using the h2tcp profile starting the h2 mem db in tcp mode.
<br>`java -jar near-miss-db-0.0.1-SNAPSHOT-exec.jar --spring.profiles.active=h2tcp --spring.config.location=../src/main/resources`

5. Start near-miss-engine using h2tcp profile.
<br>`java -jar near-miss-engine-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2tcp --spring.config.location=../src/main/resources/`

6. Start near-miss-web using h2tcp profile.
<br>`java -jar near-miss-web-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2tcp --spring.config.location=../src/main/resources`

The h2tcp (application-h2tcp.properties) will be used when running locally.
