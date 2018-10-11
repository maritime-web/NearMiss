package dk.dma.nearmiss.gpssimulator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class GpsSimulatorApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(GpsSimulatorApplication.class, args);
	}

    @Override
    public void run(String... args) throws InterruptedException {
        //noinspection InfiniteLoopStatement
        while (true) {
            logger.info("Hello GpsSimulatorApplication");
            sleep(1000);
        }
    }
}
