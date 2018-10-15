package dk.dma.nearmiss.gpssimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GpsSimulatorApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(GpsSimulatorApplication.class, args);
	}

    @Override
    public void run(String... args) throws IOException {
        logger.info("Starting GpsSimulatorApplication...");

        Gps gps = new Gps();
        gps.start();
        gps.addListener(new GpsLogSimulator(gps));

        new GpsSimulatorServer(gps).start();


        GpsSimulatorClient client = new GpsSimulatorClient();
        client.addListener(new GpsReceiver(client));
        client.start();


    }
}
