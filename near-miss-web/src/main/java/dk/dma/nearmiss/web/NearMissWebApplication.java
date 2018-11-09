package dk.dma.nearmiss.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@ComponentScan(basePackages = {"dk.dma.nearmiss.db", "dk.dma.nearmiss.web",
        "dk.dma.nearmiss.web.api", "io.swagger.configuration"})
public class NearMissWebApplication implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(NearMissWebApplication.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("Running NearMissWebApplication");
    }

}
