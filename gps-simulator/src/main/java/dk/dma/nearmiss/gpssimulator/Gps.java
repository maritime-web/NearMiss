package dk.dma.nearmiss.gpssimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static java.lang.Thread.sleep;

public class Gps extends AbstractGps {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String message;

    public void run() {
            try {
                //noinspection InfiniteLoopStatement
                while (true) {
                    message = new Date().toString();
                    notifyListeners();
                    sleep(1000);
                }
            } catch (InterruptedException e) {
                logger.info("Sleep Interruption");
            }
    }

    public String getMessage() {
        return message;
    }
}
