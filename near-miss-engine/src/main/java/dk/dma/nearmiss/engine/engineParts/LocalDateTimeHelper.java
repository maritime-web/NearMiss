package dk.dma.nearmiss.engine.engineParts;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.ZoneOffset.UTC;

public class LocalDateTimeHelper {

    public final static LocalDateTime toLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(UTC)
                .toLocalDateTime();
    }

}
