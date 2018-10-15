package dk.dma.nearmiss.gpssimulator.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class GpsSimulatorServer implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final GpsSimulator gps;
    private ServerSocket serverSocketListener;
    private int clientNumber = 0;

    GpsSimulatorServer(GpsSimulator gps) {
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
                Socket socket = serverSocketListener.accept();
                gps.addListener(new GpsSimulatorSocketConnection(socket, clientNumber++, gps));
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

    public GpsSimulator getGpsSimulator() {
        return gps;
    }
}