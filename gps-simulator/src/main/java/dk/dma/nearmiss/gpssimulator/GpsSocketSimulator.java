package dk.dma.nearmiss.gpssimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GpsSocketSimulator implements Observer {
    private final Socket socket;
    private final int clientNumber;
    private final Gps gps;

    private PrintWriter out;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    GpsSocketSimulator(Socket socket, int clientNumber, Gps gps) {
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
        print(String.format("%s : Message from GpsSocketSimulator to client# %s", time, clientNumber));
    }

}
