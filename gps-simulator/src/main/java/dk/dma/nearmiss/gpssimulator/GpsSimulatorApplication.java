package dk.dma.nearmiss.gpssimulator;

import dk.dma.nearmiss.gpssimulator.server.GpsSimulatorServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GpsSimulatorApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //private GpsSimulator gps;
    private GpsSimulatorServer gpsSimulatorServer;


    public GpsSimulatorApplication(GpsSimulatorServer gpsSimulatorServer) {
        //this.gps = gps;
        this.gpsSimulatorServer = gpsSimulatorServer;
    }

    public static void main(String[] args) {
		SpringApplication.run(GpsSimulatorApplication.class, args);
	}

    @Override
    public void run(String... args)  {
        logger.info("Starting GpsSimulatorApplication...");

        // Start GPS simulator
        new Thread(gpsSimulatorServer.getGpsSimulator()).start();

        // Start TCP server
        new Thread(gpsSimulatorServer).start();

    }
}
