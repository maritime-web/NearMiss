package dk.dma.nearmiss.gpssimulator;

import dk.dma.nearmiss.gpssimulator.client.GpsSimulatorClient;
import dk.dma.nearmiss.gpssimulator.server.GpsSimulatorServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;



@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = GpsSimulatorClient.class) })
@SpringBootApplication
public class GpsSimulatorApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private GpsSimulatorServer gpsSimulatorServer;


    public GpsSimulatorApplication(GpsSimulatorServer gpsSimulatorServer) {
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
