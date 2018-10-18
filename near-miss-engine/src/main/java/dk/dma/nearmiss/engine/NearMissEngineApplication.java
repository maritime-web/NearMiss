package dk.dma.nearmiss.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"dk.dma.nearmiss.observer", "dk.dma.nearmiss.tcp.client", "dk.dma.nearmiss.engine"})
public class NearMissEngineApplication implements CommandLineRunner {
    private final NearMissEngine engine;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public NearMissEngineApplication(NearMissEngine engine) {
        this.engine = engine;
    }

    public static void main(String[] args) {
		SpringApplication.run(NearMissEngineApplication.class, args);
	}

    @Override
    public void run(String... args) {
	    logger.info("Running NearMissEngineApplication");

        // Start GPS TCP client
        new Thread(engine.getTcpClient()).start();


    }

}
