package dk.dma.nearmiss.engine;


import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.tracker.targetTracker.TargetInfo;
import dk.dma.ais.tracker.targetTracker.TargetTracker;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TrackerTests {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String MSG1 = "$PGHP,1,2018,10,8,0,4,46,369,219,,2190073,1,*2F" + "\r\n" + "!BSVDM,1,1,,B,19qbLH001w0j6D@Q0o0m>DAH0@K9,0*2F";
    private static final int MSG1_MMSI = 664444000;

    private static final String MSG2 = "$PGHP,1,2018,10,8,0,4,30,910,219,,2190072,1,*2B" + "\r\n" +"!BSVDM,2,1,6,A,59qbLH02>r7M?MHd001<ET<PDhhE>118DiD@D01@C`O@@6U@0BDUEC5@,0*7F"+ "\r\n" + "!BSVDM,2,2,6,A,000000000000000,2*3B";

    @Test
    public void targetTracker_playground_1() throws AisMessageException, SixbitException {
        TargetTracker tracker = new TargetTracker();
        logger.info(String.format("tracker.size() = %s", tracker.size()));

        AisPacket packet = AisPacket.from(MSG1);
        int mmsi = packet.getAisMessage().getUserId();
        tracker.update(packet);
        logger.info(String.format("tracker.size() = %s", tracker.size()));
        TargetInfo info = tracker.get(MSG1_MMSI);
        assertNotNull("Expected to find info for mmsi", info);
        assertEquals("Expected to find mmsi", MSG1_MMSI, info.getMmsi());
    }
    @Test

    public void targetTracker_playground_2() throws AisMessageException, SixbitException {
        TargetTracker tracker = new TargetTracker();
        logger.info(String.format("tracker.size() = %s", tracker.size()));

        AisPacket packet = AisPacket.from(MSG2);
        int mmsi = packet.getAisMessage().getUserId();
        tracker.update(packet);
        logger.info(String.format("tracker.size() = %s", tracker.size()));
    }

}
