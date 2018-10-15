package dk.dma.nearmiss.gpssimulator;

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
    private GpsReceiver gpsReceiver;


    public GpsSimulatorApplication(GpsSimulatorServer gpsSimulatorServer, GpsReceiver gpsReceiver) {
        //this.gps = gps;
        this.gpsSimulatorServer = gpsSimulatorServer;
        this.gpsReceiver = gpsReceiver;
    }

    public static void main(String[] args) {
		SpringApplication.run(GpsSimulatorApplication.class, args);
	}

    @Override
    public void run(String... args)  {
        logger.info("Starting GpsSimulatorApplication...");

        // No more need to start GpsLogServer since it is a Spring component.

        new Thread(gpsSimulatorServer.getGpsSimulator()).start();
        new Thread(gpsSimulatorServer).start();
        new Thread(gpsReceiver.getClient()).start();


    }
}
