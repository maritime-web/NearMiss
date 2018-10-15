package dk.dma.nearmiss.gpssimulator.server;

import dk.dma.nearmiss.gpssimulator.observer.Observer;
import dk.dma.nearmiss.gpssimulator.server.GpsSimulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GpsSimulatorSocketConnection implements Observer {
    private final Socket socket;
    private final int clientNumber;
    private final GpsSimulator gps;

    private PrintWriter out;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    GpsSimulatorSocketConnection(Socket socket, int clientNumber, GpsSimulator gps) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        this.gps = gps;
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
        String time = gps.getMessage();
        print(String.format("%s : Message from GpsSimulatorSocketConnection to client# %s", time, clientNumber));
    }

}
