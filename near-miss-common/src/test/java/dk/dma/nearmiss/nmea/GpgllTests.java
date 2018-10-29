package dk.dma.nearmiss.nmea;

import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class GpgllTests {

    @Test
    public void testInstant() {
        String expectedUtcTime = "113429";
        Instant instant = ZonedDateTime.parse("2015-08-13T11:34:29.62+00:00").toInstant();


        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HHmmss").withZone(ZoneId.of("UTC"));
        String formattedInstant = formatter1.format(instant);
        assertEquals("Expected instant formatted right", expectedUtcTime, formattedInstant);
    }

}
