package dk.dma.nearmiss.gpssimulator;

import dk.dma.nearmiss.gpssimulator.location.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

    @Value("${gps.trip.start.name:#{'Hundested'}}")
    private String tripStartName;
    @Value("${gps.trip.start.lat:#{56.02250000}}")
    private double tripStartLat;
    @Value("${gps.trip.start.lon:#{11.73222222}}")
    private double tripStartLon;

    @Value("${gps.trip.end.name:#{'Göteborg'}}")
    private String tripEndName;
    @Value("${gps.trip.end.lat:#{57.67500000}}")
    private double tripEndLat;
    @Value("${gps.trip.end.lon:#{11.73222222}}")
    private double tripEndLon;

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

    public Location getTripStart() {
        return new Location(tripStartName, tripStartLat, tripStartLon);
    }

    public Location getTripEnd() {
        return new Location(tripEndName, tripEndLat, tripEndLon);
    }

}
