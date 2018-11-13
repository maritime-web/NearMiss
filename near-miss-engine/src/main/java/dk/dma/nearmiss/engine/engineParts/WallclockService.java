package dk.dma.nearmiss.engine.engineParts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service to maintain the current time (current wallclock).
 * Use this service instead of direct calls to <built-in-class>#now()
 */
@SuppressWarnings("WeakerAccess")
@Service
public class WallclockService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AtomicReference<LocalDateTime> currentDateTime;

    public WallclockService() {
        this.currentDateTime = new AtomicReference<>(LocalDateTime.MIN);
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime.get();
    }

    public void setCurrentDateTime(LocalDateTime newCurrentTime) {
        if (newCurrentTime.isBefore(currentDateTime.get()))
            logger.error("Wallclock not adjusted: " + newCurrentTime + " is before " + currentDateTime.get());
        else
            currentDateTime.getAndSet(newCurrentTime);

        logger.info("Time is now " + currentDateTime.get());
    }

}
