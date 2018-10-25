package dk.dma.nearmiss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("unused")
@Component
public class GpsSimulatorConfiguration {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Should be in format HH:mm:ss or omitted.
    @Value("${gps.start.time:#{null}}")
    private String startTimeString;
    private Duration startTimeOffset;
    private final LocalTime startupTime;

    public GpsSimulatorConfiguration() {
        this.startupTime = LocalTime.now(ZoneOffset.UTC);
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    private void init() {
        if (startTimeString == null) {
            startTimeOffset = Duration.ZERO;
        } else {
            LocalTime startTime = LocalTime.parse(startTimeString, formatter);
            startTimeOffset = Duration.between(startupTime, startTime);
        }
    }

    public Duration getStartTimeOffset() {
        if (startTimeOffset == null) {
            init();
        }
        return startTimeOffset;
    }
}
