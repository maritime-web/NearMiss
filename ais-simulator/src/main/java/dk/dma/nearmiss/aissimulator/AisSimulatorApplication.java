package dk.dma.nearmiss.aissimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"dk.dma.nearmiss.observer", "dk.dma.nearmiss.tcp", "dk.dma.nearmiss.aissimulator"})
public class AisSimulatorApplication implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private AisSimulator aisSimulator;
    private AisSimulatorServer aisSimulatorServer;

	public AisSimulatorApplication(AisSimulator aisSimulator, AisSimulatorServer aisSimulatorServer) {
		this.aisSimulator = aisSimulator;
		this.aisSimulatorServer = aisSimulatorServer;
	}

	public static void main(String[] args) {
		SpringApplication.run(AisSimulatorApplication.class, args);
	}

	@Override
	public void run(String... args) {
        logger.info("Starting AisSimulatorApplication...");

		// Start TCP server
		new Thread(aisSimulatorServer).start();

        // Start GPS TCP client
		new Thread(aisSimulator.getTcpClient()).start();

	}

}
