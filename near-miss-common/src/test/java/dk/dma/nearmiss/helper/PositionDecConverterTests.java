package dk.dma.nearmiss.helper;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;

public class PositionDecConverterTests {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private PositionDecConverter NULL_CONVERTER = new PositionDecConverter(null, null);
    private static final String STR_LAT = "5601.60,N";
    private static final String STR_LON = "01143.60,E";

    @Test
    public void playground_lat_1() {
        String strLat = STR_LAT;
        String degrees = strLat.substring(0, 2);
        logger.info(String.format("Degrees: %s", degrees));

        String minutes = strLat.substring(2, 4);
        logger.info(String.format("Minutes: %s", minutes));

        String seconds = strLat.substring(5, 7);
        logger.info(String.format("Seconds: %s", seconds));

        String lastLetter = strLat.substring(strLat.length() -1);
        logger.info(String.format("Last letter: %s", lastLetter));
    }

    @Test
    public void playground_lat_2() {
        String strLat = STR_LAT;
        logger.info(String.format("Initial string latitude: %s", strLat));

        double degrees = NULL_CONVERTER.degrees(strLat, true);
        logger.info(String.format("Degrees: %s", degrees));

        double minutes = NULL_CONVERTER.minutes(strLat, true);logger.info(String.format("Minutes: %s", minutes));

        double seconds = NULL_CONVERTER.seconds(strLat, true);
        logger.info(String.format("Seconds: %s", seconds));

        double lastLetter = NULL_CONVERTER.letter(strLat);
        logger.info(String.format("Last letter: %s", lastLetter));
    }

    @Test
    public void playground_lon() {
        String strLon = STR_LON;
        logger.info(String.format("Initial string longitude: %s", strLon));

        double degrees = NULL_CONVERTER.degrees(strLon, false);
        logger.info(String.format("Degrees: %s", degrees));

        double minutes = NULL_CONVERTER.minutes(strLon, false);
        logger.info(String.format("Minutes: %s", minutes));

        double seconds = NULL_CONVERTER.seconds(strLon, false);
        logger.info(String.format("Seconds: %s", seconds));

        double lastLetter = NULL_CONVERTER.letter(strLon);
        logger.info(String.format("Last letter: %s", lastLetter));
    }

    @Test
    public void positionDecimal_toDec_and_back() {
        String strLat = "5712.40,N";
        String strLon = "01143.60,E";

        logger.info(String.format("Initial string latitude: %s", strLat));
        logger.info(String.format("Initial string longitude: %s", strLon));

        double decimalLat = NULL_CONVERTER.positionDecimal(strLat, true);
        logger.info(String.format("Decimal latitude: %s", decimalLat));

        double decimalLon = NULL_CONVERTER.positionDecimal(strLon, false);
        logger.info(String.format("Decimal longitude: %s", decimalLon));

        String degreesMinustesSeconds = new PositionDmsConverter(decimalLat, decimalLon).convert();
        logger.info(String.format("Back to degrees, minutes, seconds: %s", degreesMinustesSeconds));
    }

    @Test
    public void convert_and_back() {
        logger.info(String.format("Initial string latitude: %s", STR_LAT));
        logger.info(String.format("Initial string longitude: %s", STR_LON));

        PositionDecConverter conv = new PositionDecConverter(STR_LAT, STR_LON);
        Position pos = conv.convert();
        assertNotNull("Expected to receive position back", pos);

        logger.info(String.format("Decimal latitude: %s", pos.getLat()));
        logger.info(String.format("Decimal longitude: %s", pos.getLon()));

        String degreesMinustesSeconds = new PositionDmsConverter(pos.getLat(), pos.getLon()).convert();
        logger.info(String.format("Back to degrees, minutes, seconds: %s", degreesMinustesSeconds));
    }




}



