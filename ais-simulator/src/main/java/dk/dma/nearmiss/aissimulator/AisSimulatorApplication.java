package dk.dma.nearmiss.aissimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class AisSimulatorApplication implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(AisSimulatorApplication.class, args);
	}

	@Override
	public void run(String... args) throws InterruptedException {
		//noinspection InfiniteLoopStatement
		while (true) {
			logger.info("Hello AisSimulatorApplication");
			sleep(1000);
		}
	}

}
