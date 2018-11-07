package dk.dma.nearmiss.pilotplugsimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class LineReader implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String remoteHost;
    private final int remotePort;
    private final Queue<String> messageQueue;

    LineReader(Queue<String> messageQueue, String remoteHost, int remotePort) {
        this.messageQueue = messageQueue;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    @Override
    public void run() {
        logger.info("FileReader for {}:{} running.", remoteHost, remotePort);

        try {
            Socket client = connectForRead(remoteHost, remotePort);
            Scanner scanner = new Scanner(client.getInputStream());

            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    String line = scanner.nextLine();
                    logger.info("From {}:{}: {}", remoteHost, remotePort, line);
                    messageQueue.add(line);

                    final int n = messageQueue.size();
                    if (n > 100 && n % 100 == 0)
                        logger.warn("Message queue size is {}", n);
                } catch (NoSuchElementException e) {
                    logger.warn(e.getMessage());

                    client.close();
                    logger.info("Connection to {}:{} closed.", remoteHost, remotePort);

                    client = connectForRead(remoteHost, remotePort);
                    scanner = new Scanner(client.getInputStream());
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        logger.info("FileReader for {}:{} finishing.", remoteHost, remotePort);
    }

    private Socket connectForRead(String remoteHost, int remotePort) {
        Socket client = null;

        do {
            try {
                client = new Socket(remoteHost, remotePort);
                logger.info("Connected to {}:{} for read.", remoteHost, remotePort);
            } catch (IOException e) {
                logger.error("Connection to {}:{}: {}", remoteHost, remotePort, e.getMessage());
            }

            if (client == null) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage());
                }
            }
        } while (client == null);

        return client;
    }
}
