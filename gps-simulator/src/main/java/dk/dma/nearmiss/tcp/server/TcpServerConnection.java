package dk.dma.nearmiss.tcp.server;

import dk.dma.nearmiss.simulator.MessageProvider;
import dk.dma.nearmiss.observer.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpServerConnection implements Observer {
    private final Socket socket;
    private final int clientNumber;
    private final MessageProvider simulator;

    private PrintWriter out;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    TcpServerConnection(Socket socket, int clientNumber, MessageProvider simulator) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        this.simulator = simulator;
        logger.info("New connection with client# " + clientNumber + " at " + socket);
    }

    private void print(String message) {
        try {
            if (out == null) {
                out = new PrintWriter(socket.getOutputStream(), true);
            }
            out.println(message);
        } catch (IOException e) {
            logger.info("Error handling client# " + clientNumber + ": " + e);
        }
    }

    @Override
    public void update() {
        String time = simulator.getMessage();
        print(String.format("%s : Message from GpsSimulatorSocketConnection to client# %s", time, clientNumber));
    }

}
