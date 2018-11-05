package dk.dma.nearmiss.engine;


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

    @Test
    public void targetTracker_playground() {
        TargetTracker tracker = new TargetTracker();
        logger.info(String.format("tracker.size() = %s", tracker.size()));

        AisPacket packet = AisPacket.from(MSG1);

        tracker.update(packet);
        logger.info(String.format("tracker.size() = %s", tracker.size()));
        TargetInfo info = tracker.get(MSG1_MMSI);
        assertNotNull("Expected to find info for mmsi", info);
        assertEquals("Expected to find mmsi", MSG1_MMSI, info.getMmsi());
    }

}
