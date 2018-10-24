package dk.dma.nearmiss.aissimulator;


import dk.dma.ais.proprietary.GatehouseFactory;
import dk.dma.ais.proprietary.GatehouseSourceTag;
import dk.dma.ais.sentence.SentenceLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AisLibTests {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PGHP_TIME_STAMP = "$PGHP,1,2018,10,8,0,4,37,462,219,,2190074,1,*22";

    @Test
    public void test1() {
        SentenceLine sentenceLine = new SentenceLine(PGHP_TIME_STAMP);

        GatehouseFactory factory = new GatehouseFactory();
        GatehouseSourceTag tag = (GatehouseSourceTag) GatehouseFactory.parseTag(sentenceLine);

        Instant instant = tag.getTimestamp().toInstant();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss").withZone(ZoneId.of("UTC"));
        logger.info(String.format("Timestamp is: %s", formatter.format(instant)));


        logger.info(factory.getTag(sentenceLine).getSentence());
    }

}
