package dk.dma.nearmiss.aissimulator;

import dk.dma.ais.proprietary.GatehouseFactory;
import dk.dma.ais.proprietary.GatehouseSourceTag;
import dk.dma.ais.sentence.SentenceLine;
import dk.dma.nearmiss.nmea.GpgllHelper;
import dk.dma.nearmiss.observer.Observer;
import dk.dma.nearmiss.simulator.Simulator;
import dk.dma.nearmiss.tcp.client.TcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class AisSimulator extends Simulator implements Observer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TcpClient tcpClient;
    private final AisDataReader aisDataReader;
    private LocalDate currentLocalDate;

    private String input;
    private String output;

    public AisSimulator(TcpClient tcpClient, AisDataReader aisDataReader) {
        this.tcpClient = tcpClient;
        this.aisDataReader = aisDataReader;

        AisDataLine aisDataLine = nextAisDataLine();
        currentLocalDate = aisDataLine != null ? getMessageDateTime(aisDataLine.getTime()).toLocalDate() : null;
        tcpClient.addListener(this);
    }

    @Override
    public synchronized void update() {
        input = tcpClient.getMessage();
        logger.info(String.format("AisSimulator Received: %s", input));
        run(); // run AisServer server part.
    }

    TcpClient getTcpClient() {
        return tcpClient;
    }

    @Override
    public String getMessage() {
        return output;
    }

    @Override
    public void run() {
        AisDataLine aisDataLine = aisDataReader.getLine();
        if (aisDataLine == null) {
            logger.debug("No more AIS data");
            return;
        }

        logger.trace(String.format("Next message is: %s\r\n", aisDataLine.getTimedMessage()));
        LocalDateTime messageDateTime = getMessageDateTime(aisDataLine.getTime());
        logger.trace(String.format("Message DateTime is: %s", messageDateTime));
        LocalDateTime gpsDateTime = new GpgllHelper(input).getLocalDateTime(currentLocalDate);
        logger.trace(String.format("GPS DateTime is: %s", gpsDateTime));

        while (aisDataLine != null && gpsDateTime.isAfter(messageDateTime)) {
            output = aisDataLine.getTimedMessage();
            logger.debug(String.format("Brodcasting message: %s", output));
            notifyListeners();
            aisDataLine = nextAisDataLine();
            messageDateTime = aisDataLine != null ? getMessageDateTime(aisDataLine.getTime()) : null;
        }
    }

    private AisDataLine nextAisDataLine() {
        boolean messageFound = aisDataReader.forward();
        if (!messageFound) {
            logger.trace("Did not find another AIS message");
        }
        return aisDataReader.getLine();
    }

    private LocalDateTime getMessageDateTime(String time) {
        SentenceLine sentenceLine = new SentenceLine(time);
        GatehouseSourceTag tag = (GatehouseSourceTag) GatehouseFactory.parseTag(sentenceLine);
        return LocalDateTime.ofInstant(tag.getTimestamp().toInstant(), ZoneId.of("UTC"));
    }

}
