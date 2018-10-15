package dk.dma.nearmiss.aissimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.lang.Thread.sleep;

@Component
public class AisSimulator implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            logger.info("Hello AisSimulatorApplication");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                logger.info("AisSimulator loop interrupted.");
            }
        }
    }
}
