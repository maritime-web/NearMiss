package dk.dma.nearmiss.gpssimulator;

import dk.dma.nearmiss.GpsSimulatorConfiguration;
import dk.dma.nearmiss.gpssimulator.location.GeoHelper;
import dk.dma.nearmiss.gpssimulator.location.Location;
import dk.dma.nearmiss.gpssimulator.location.Route;
import dk.dma.nearmiss.gpssimulator.location.RouteSimulator;
import dk.dma.nearmiss.nmea.Gpgll;
import dk.dma.nearmiss.simulator.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.ZoneOffset;

import static java.lang.Thread.sleep;

/**
 * This class is the actual GPS simulator.
 * It will notify all listeners every given time period (currently once per second).
 * Listeners are able to ask the GPS simulator of the current position.
 */
@Component
public class GpsSimulator extends Simulator {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String message;
    private final GpsSimulatorConfiguration configuration;

    public GpsSimulator(GpsSimulatorConfiguration configuration) {
        this.configuration = configuration;
    }

    private String getRemainingDistance(Location currentLocation, Location lastWaypoint) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(GeoHelper.calcGeoDistanceInKm(currentLocation, lastWaypoint));
    }

    private void makeTrip(Route trip) throws InterruptedException {
        RouteSimulator sim = new RouteSimulator(trip);
        //noinspection InfiniteLoopStatement
        while (!sim.hasArrived()) {

            //OBS. High speed. Loop five times to optain approx. 20 knots.(?)
            for (int i = 0; i < 3; ++i) { // Iterate to create speed. Break if arrived.
                sim.move();
                if (sim.hasArrived()) break;
            }

            Gpgll gpgll = new Gpgll(sim.currentLocation.getLatitude(), sim.currentLocation.getLongitude(), getSimulatedTime());
            message = gpgll.toString();
            logger.info(String.format("At %s, remaining distance to %s is %s km",
                    gpgll.getFormattedTime(), trip.lastWaypoint().getName(), getRemainingDistance(sim.currentLocation, trip.lastWaypoint())));
            logger.debug(String.format("Broadcasting message: %s", message));
            notifyListeners();

            sleep(1000);
        }
    }


    private LocalTime getSimulatedTime() {
        LocalTime currentTimeUtc = LocalTime.now(ZoneOffset.UTC);
        return currentTimeUtc.plus(configuration.getStartTimeOffset());
    }

    public void run() {
        Location tripStart = configuration.getTripStart();
        Location tripEnd = configuration.getTripEnd();
        Route route = new Route(tripStart, tripEnd);

        //noinspection InfiniteLoopStatement
        for (; ; ) {
            try {
                logger.info(String.format("Sailing from %s to %s.", route.getInitialLocation().getName(), route.lastWaypoint().getName()));
                makeTrip(route);
                logger.info(String.format("Arrived at %s.", route.lastWaypoint().getName()));
                logger.info("Turning around.");
                route = new Route(route.lastWaypoint(), route.getInitialLocation());
            } catch (InterruptedException e) {
                logger.info("Sleep Interruption");
            }
        }
    }

    public String getMessage() {
        return message;
    }
}
