package dk.dma.nearmiss.aissimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"dk.dma.nearmiss.gpssimulator.observer", "dk.dma.nearmiss.gpssimulator.client", "dk.dma.nearmiss.aissimulator"})
public class AisSimulatorApplication implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private AisSimulator aisSimulator;

	public AisSimulatorApplication(AisSimulator aisSimulator) {
		this.aisSimulator = aisSimulator;
	}

	public static void main(String[] args) {
		SpringApplication.run(AisSimulatorApplication.class, args);
	}

	@Override
	public void run(String... args) {
        logger.info("Starting AisSimulatorApplication...");
		new Thread(aisSimulator.getGpsSimulatorClient()).start();
	}

}
