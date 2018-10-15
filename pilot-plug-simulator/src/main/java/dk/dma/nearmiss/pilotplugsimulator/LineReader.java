package dk.dma.nearmiss.pilotplugsimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.Scanner;

public class LineReader implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String remoteHost;
    private final int remotePort;
    private final Queue<String> messageQueue;

    public LineReader(Queue<String> messageQueue, String remoteHost, int remotePort) {
        this.messageQueue = messageQueue;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    @Override
    public void run() {
        logger.info("FileReader for {}:{} running.", remoteHost, remotePort);

        try {
            Socket client = new Socket(remoteHost, remotePort);
            Scanner scanner = new Scanner(client.getInputStream());

            while (true) {
                String line = scanner.nextLine();
                messageQueue.add(line);

                final int n = messageQueue.size();
                if (n > 100 && n % 100 == 0)
                    logger.warn("Message queue size is {}", n);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        logger.info("FileReader for {}:{} finishing.", remoteHost, remotePort);
    }
}
