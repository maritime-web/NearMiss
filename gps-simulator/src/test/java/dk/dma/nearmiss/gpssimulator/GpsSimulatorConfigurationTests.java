package dk.dma.nearmiss.gpssimulator;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GpsSimulatorConfigurationTests {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GpsSimulatorConfiguration conf;

    @Test
    @Ignore
    public void testLocalTime() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime expectedTime = LocalTime.parse("07:00:00", formatter);
        logger.info(expectedTime.format(ISO_LOCAL_TIME));
        logger.info(expectedTime.toString());

        LocalTime currentTime = LocalTime.now();
        logger.info(String.format("Current time: %s", currentTime.format(ISO_LOCAL_TIME)));
        LocalTime currentTimeUtc = LocalTime.now(ZoneOffset.UTC);
        logger.info(String.format("Current time UTC: %s", currentTimeUtc.format(ISO_LOCAL_TIME)));
        logger.info(String.format("Current time UTC: %s", currentTimeUtc.format(formatter)));

    }

}
