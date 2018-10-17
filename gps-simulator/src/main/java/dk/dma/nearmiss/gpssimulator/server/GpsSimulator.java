package dk.dma.nearmiss.gpssimulator.server;

import dk.dma.nearmiss.gpssimulator.location.GeoHelper;
import dk.dma.nearmiss.gpssimulator.location.Location;
import dk.dma.nearmiss.gpssimulator.location.Route;
import dk.dma.nearmiss.gpssimulator.location.RouteSimulator;
import dk.dma.nearmiss.gpssimulator.nmea.Gpgll;
import dk.dma.nearmiss.gpssimulator.observer.AbstractSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.lang.Thread.sleep;

/**
 * This class is the actual GPS simulator.
 * It will notify all listeners every given time period (currently once per second).
 * Listeners are able to ask the GPS simulator of the current position.
 */
@Component
public class GpsSimulator extends AbstractSubject implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String message;

    private String getRemainingDistance(Location currentLocation, Location lastWaypoint) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(GeoHelper.calcGeoDistanceInKm(currentLocation, lastWaypoint));
    }

    private void makeTrip(Route trip) throws InterruptedException {
        RouteSimulator sim = new RouteSimulator(trip);
        //noinspection InfiniteLoopStatement
        while (!sim.hasArrived()) {

            logger.info(String.format("Remaining distance to %s is %s km", trip.lastWaypoint().getName(), getRemainingDistance(sim.currentLocation, trip.lastWaypoint())));
            //OBS. High speed. Loop five times to optain approx. 20 knots.(?)
            for (int i = 0; i < 500; ++i) { // Iterate to create speed. Break if arrived.
                sim.move();
                if (sim.hasArrived()) break;
            }

            message = new Gpgll(sim.currentLocation).toString();
            notifyListeners();
            sleep(1000);
        }
    }

    public void run() {
        Location tripStart = new Location("Hundested", 56.02250000, 11.73222222);
        Location tripEnd = new Location("Göteborg", 57.67500000, 11.73222222);
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

    String getMessage() {
        return message;
    }
}
