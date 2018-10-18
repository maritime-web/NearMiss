package dk.dma.nearmiss.tcp.server;

import dk.dma.nearmiss.simulator.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class TcpServer implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Simulator simulator;
    private final TcpServerConfiguration configuration;
    private ServerSocket serverSocketListener;
    private int clientNumber = 0;

    public TcpServer(Simulator simulator, TcpServerConfiguration configuration) {
        this.simulator = simulator;
        this.configuration = configuration;
        init();
    }

    private void init() {
        try {
            logger.info(String.format("Using %s as localPort.", configuration.getLocalPort()));
            serverSocketListener = new ServerSocket(configuration.getLocalPort());
        } catch (IOException e) {
            logger.error(String.format("Could not create new server socket on port %s", configuration.getLocalPort()));
        }
    }

    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = serverSocketListener.accept();
                simulator.addListener(new TcpServerConnection(socket, clientNumber++, simulator));
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

    public Simulator getSimulator() {
        return simulator;
    }

}
