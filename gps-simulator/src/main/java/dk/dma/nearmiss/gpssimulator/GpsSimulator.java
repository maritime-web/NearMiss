package dk.dma.nearmiss.gpssimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GpsSimulator extends Thread {
    private Socket socket;
    private int clientNumber;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    GpsSimulator(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        logger.info("New connection with client# " + clientNumber + " at " + socket);
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            //noinspection InfiniteLoopStatement
            while (true) {
                out.println("Message from GpsSimulator to client# " + clientNumber);
                sleep(1000);
            }
        } catch (IOException e) {
            logger.info("Error handling client# " + clientNumber + ": " + e);
        } catch (InterruptedException e) {
            logger.info("Interruption on client# " + clientNumber + ": " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.info("Couldn't close a socket, what's going on?");
            }
            logger.info("Connection with client# " + clientNumber + " closed");
        }
    }
}
