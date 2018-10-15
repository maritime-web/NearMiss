package dk.dma.nearmiss.gpssimulator.client;

import dk.dma.nearmiss.gpssimulator.observer.AbstractSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@Component
public class GpsSimulatorClient extends AbstractSubject implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("FieldCanBeLocal")
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
        boolean continueLoop = true;
        while (continueLoop) {
            if (input != null) {
                try {
                    answer = input.readLine();
                    notifyListeners();
                } catch (IOException e) {
                    logger.error("Client: Error reading from server");
                }
            } else {
                logger.error("Client: No stream to read from");
                continueLoop = false;
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    public String getAnswer() {
        return answer;
    }
}
