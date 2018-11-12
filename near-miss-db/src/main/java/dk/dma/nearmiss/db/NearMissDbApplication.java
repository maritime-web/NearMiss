package dk.dma.nearmiss.db;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.sql.SQLException;

@SpringBootApplication
public class NearMissDbApplication extends SpringBootServletInitializer {
    private static Logger logger = LoggerFactory.getLogger(NearMissDbApplication.class);


    public static void main(String[] args) {
		startH2Server();
		SpringApplication.run(NearMissDbApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		startH2Server();
		return application.sources(NearMissDbApplication.class);
	}

	private static void startH2Server() {
        try {
			Server h2Server = Server.createTcpServer().start();
			if (h2Server.isRunning(true)) {
                logger.info("H2 server was started and is running.");
			} else {
				throw new RuntimeException("Could not start H2 server.");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to start H2 server: ", e);
		}
	}
}
