package dk.dma.nearmiss.tcp.client;

import dk.dma.nearmiss.simulator.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@Component
public class TcpClient extends Simulator {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("FieldCanBeLocal")
    private Socket socket;
    private BufferedReader input;
    private String answer;
    private final TcpClientConfiguration configuration;

    TcpClient(TcpClientConfiguration configuration) {
        this.configuration = configuration;
        init();
    }

    private void init() {
        try {
            logger.debug(String.format("Creating to connection towards %s:%s", configuration.getHost(), configuration.getRemotePort()));
            socket = new Socket(configuration.getHost(), configuration.getRemotePort());
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
                    try {
                        notifyListeners();
                    } catch (Throwable t) {
                        logger.error(t.getMessage(), t);
                    }
                } catch (IOException e) {
                    logger.error("Client: Error reading from server");
                }
            } else {
                logger.error("Client: No stream to read from");
                continueLoop = false;
            }
        }
    }

    public String getMessage() {
        return answer;
    }
}
