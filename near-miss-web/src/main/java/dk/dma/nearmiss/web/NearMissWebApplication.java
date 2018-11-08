package dk.dma.nearmiss.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.Formatter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@EnableSwagger2
@SpringBootApplication
@ComponentScan(basePackages = {"dk.dma.nearmiss.db", "dk.dma.nearmiss.web",
        "dk.dma.nearmiss.rest.generated.api", "io.swagger.configuration"})
public class NearMissWebApplication implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /*
    @Bean

    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {
            @Override
            public LocalDate parse(String text, Locale locale) throws ParseException {
                return LocalDate.parse(text, DateTimeFormatter.ISO_INSTANT);
            }

            @Override
            public String print(LocalDate object, Locale locale) {
                return DateTimeFormatter.ISO_INSTANT.format(object);
            }
        };
    }
    */

    public static void main(String[] args) {
        SpringApplication.run(NearMissWebApplication.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("Running NearMissWebApplication");
    }

}
