package dk.dma.nearmiss.engine;

public class NearMissEngineTest {

    /*
        TODO test case with invalid NMEA checksum
        $PGHP,1,2018,10,7,22,1,4,0,,804,,1,*17s:NorSat_1,c:1538949664*48__r__n!AIVDM,1,1,,C,KkuJQI0@Hl4>IP<`,0*38
        2018-11-08 17:27:09.458 TRACE 50802 --- [      Thread-11] dk.dma.nearmiss.engine.NearMissEngine    : Updating other ship
        2018-11-08 17:27:09.458 ERROR 50802 --- [      Thread-11] dk.dma.ais.proprietary.GatehouseFactory  : Error in Gatehouse proprietary tag: "$PGHP,1,2018,10,7,22,1,4,0,,804,,1,*17s:NorSat_1,c:1538949664*48": Wrong checksum field: 48: Should be: 23
    */

}