package dk.dma.nearmiss.pilotplugsimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class LineDistributor implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BlockingQueue<String> messageQueue;
    private final BlockingQueue<PrintWriter> printWriters;
    private long messageCounter = 0;

    public LineDistributor(BlockingQueue<String> messageQueue, BlockingQueue<PrintWriter> printWriters) {
        this.messageQueue = messageQueue;
        this.printWriters = printWriters;
    }

    @Override
    public void run() {
        logger.info("LineDistributor running.");

        while (true) {
            try {
                String line = messageQueue.take();
                printWriters.forEach(writer -> {
                    writer.println(line);
                    writer.flush();
                });

                messageCounter++;
                if (messageCounter % 100L == 0L)
                    logger.info("{} messages distributed", messageCounter);
            } catch (InterruptedException e) {
                logger.warn(e.getMessage(), e);
            }
        }

        //logger.info("LineDistributor finishing.");
    }

}
