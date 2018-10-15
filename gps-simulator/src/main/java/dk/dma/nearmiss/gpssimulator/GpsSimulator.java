package dk.dma.nearmiss.gpssimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.lang.Thread.sleep;

/**
 * This class is the actual GPS simulator.
 * It will notify all listeners every given time period (currently once per second).
 * Listeners are able to ask the GPS simulator of the current position.
 */
@Component
public class GpsSimulator extends AbstractSubject implements Runnable {
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

    String getMessage() {
        return message;
    }
}
