package dk.dma.nearmiss.gpssimulator;

import dk.dma.nearmiss.tcp.client.TcpClient;
import dk.dma.nearmiss.tcp.server.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = {"dk.dma.nearmiss"},
        excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TcpClient.class) })
public class GpsSimulatorApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private TcpServer gpsSimulatorServer;

    public GpsSimulatorApplication(TcpServer gpsSimulatorServer) {
        this.gpsSimulatorServer = gpsSimulatorServer;
    }

    public static void main(String[] args) {
		SpringApplication.run(GpsSimulatorApplication.class, args);
	}

    @Override
    public void run(String... args)  {
        logger.info("Starting GpsSimulatorApplication...");

        // Start GPS simulator
        new Thread(gpsSimulatorServer.getSimulator()).start();

        // Start TCP server
        new Thread(gpsSimulatorServer).start();

    }
}
