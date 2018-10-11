package dk.dma.nearmiss.gpssimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.ServerSocket;

@SpringBootApplication
public class GpsSimulatorApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(GpsSimulatorApplication.class, args);
	}

    @SuppressWarnings({"InfiniteLoopStatement", "LoopStatementThatDoesntLoop"})
    @Override
    public void run(String... args) throws IOException {
        logger.info("Starting GpsSimulatorApplication...");

        ServerSocket listener = new ServerSocket(9898);
        //noinspection TryFinallyCanBeTryWithResources
        try {
            int clientNumber = 0;
            while (true) {
                new GpsSimulator(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }
}
