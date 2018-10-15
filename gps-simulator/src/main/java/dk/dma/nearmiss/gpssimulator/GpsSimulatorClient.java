package dk.dma.nearmiss.gpssimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class GpsSimulatorClient extends AbstractGps {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Socket socket;
    private BufferedReader input;
    private String answer;

    GpsSimulatorClient() {
        init();
    }

    private void init() {
        try {
            socket = new Socket("localhost", 9898);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            logger.error("Client: Error creating socket/stream");
        }
    }

    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            if (input != null) {
                try {
                    answer = input.readLine();
                    notifyListeners();
                } catch (IOException e) {
                    logger.error("Client: Error reading from server");
                }
            } else {
                logger.error("Client: No stream to read from");
            }
        }
    }

    public String getAnswer() {
        return answer;
    }
}
