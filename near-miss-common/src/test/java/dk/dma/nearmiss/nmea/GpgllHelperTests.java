package dk.dma.nearmiss.nmea;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class GpgllHelperTests {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getLocalTime() {
        String gpgllString = "$GPGLL,5620.20,N,01143.60,E,071127,A,*XX";

        LocalTime localTime = new GpgllHelper(gpgllString).getLocalTime();

        assertEquals("Expected correct hour", 7, localTime.getHour());
        assertEquals("Expected correct minutes", 11, localTime.getMinute());
        assertEquals("Expected correct seconds", 27, localTime.getSecond());
    }

    @Test
    public void getLocalDateTime() {

        String gpgllString = "$GPGLL,5620.20,N,01143.60,E,071127,A,*XX";
        LocalDate localDate = LocalDate.now().withYear(2018).withMonth(10).withDayOfMonth(25);

        LocalDateTime localDateTime = new GpgllHelper(gpgllString).getLocalDateTime(localDate);

        assertEquals("Expected correct hour", 7, localDateTime.getHour());
        assertEquals("Expected correct minutes", 11, localDateTime.getMinute());
        assertEquals("Expected correct seconds", 27, localDateTime.getSecond());

        assertEquals("Expected correct year", 2018, localDateTime.getYear());
        assertEquals("Expected correct month", 10, localDateTime.getMonthValue());
        assertEquals("Expected correct day of month", 25, localDateTime.getDayOfMonth());
    }

}
