package dk.dma.nearmiss.gpssimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class GpsSimulatorServer extends Thread {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Gps gps;
    private ServerSocket serverSocketListener;
    private int clientNumber = 0;

    GpsSimulatorServer(Gps gps) {
        this.gps = gps;
        init();
    }

    private void init() {
        try {
            serverSocketListener = new ServerSocket(9898);
        } catch (IOException e) {
            logger.error("Could not create new server socket on port 9898");
        }
    }

    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                gps.addListener(new GpsSocketSimulator(serverSocketListener.accept(), clientNumber++, gps));
            }
        } catch (IOException e) {
            logger.error(String.format("Error communicating with server socket: %s", e.getMessage()));
        } finally {
            close();
        }
    }

    private void close() {
        if (serverSocketListener != null) {
            try {
                serverSocketListener.close();
            } catch (IOException e) {
                logger.error("Could not close server socket");
            }
        }
    }

}
