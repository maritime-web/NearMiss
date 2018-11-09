package dk.dma.nearmiss.web;

import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertNotNull;

public class OffsetDateTimeTests {

    @Test
    public void parse() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        OffsetDateTime from = OffsetDateTime.parse("2018-11-09T12:34:12.673Z", formatter);
        assertNotNull("Expected from to have value", from);
    }

}
