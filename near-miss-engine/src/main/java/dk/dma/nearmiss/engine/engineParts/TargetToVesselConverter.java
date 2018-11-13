package dk.dma.nearmiss.engine.engineParts;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.tracker.targetTracker.TargetInfo;
import dk.dma.nearmiss.engine.Vessel;
import dk.dma.nearmiss.engine.geometry.VesselGeometryService;
import dk.dma.nearmiss.helper.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Function;

import static java.lang.Double.NaN;
import static java.time.ZoneOffset.UTC;

@Component
public class TargetToVesselConverter implements Function<TargetInfo, Vessel> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VesselGeometryService geometryService;

    public TargetToVesselConverter(VesselGeometryService geometryService) {
        this.geometryService = geometryService;
    }

    @Override
    public Vessel apply(TargetInfo targetInfo) {
        return toVessel(targetInfo);
    }

    /** Convert TargetInfo to Vessel */
    private Vessel toVessel(TargetInfo t) {
        String name = null;
        int dimPort = 0;
        int dimStarboard = 0;
        int dimBow = 0;
        int dimStern = 0;

        if (t.hasStaticInfo()) {
            AisPacket staticAisPacket = t.getStaticAisPacket1();
            try {
                AisMessage aisMessage = staticAisPacket.getAisMessage();

                if (aisMessage instanceof AisStaticCommon) {
                    AisStaticCommon staticData = (AisStaticCommon) aisMessage;
                    name = staticData.getName().replace("@", "").trim();
                    dimPort = staticData.getDimPort();
                    dimStarboard = staticData.getDimStarboard();
                    dimBow = staticData.getDimBow();
                    dimStern = staticData.getDimStern();
                }
            } catch (AisMessageException | SixbitException e) {
                logger.error(e.getMessage());
            }
        }

        Vessel v = new Vessel(t.getMmsi());
        v.setName(name);
        v.setLoa(dimStern + dimBow);
        v.setBeam(dimPort + dimStarboard);

        LocalDateTime lastPositionReport = LocalDateTime.ofEpochSecond(t.getPositionTimestamp()/1000, (int) ((t.getPositionTimestamp()%1000)*1000000), UTC);
        logger.debug("Target {} last report {}", t.getMmsi(), lastPositionReport);
        v.setLastPositionReport(lastPositionReport);

        if (t.hasPositionInfo()) {
            v.setSog(t.getSog() / 10);
            v.setCog(t.getCog() / 10);
            v.setHdg(t.getHeading() == 511 ? NaN : t.getHeading());

            Position gpsReceiverPosition = new Position(t.getPosition().getLatitude(), t.getPosition().getLongitude());
            Position geometricCenterPosition = geometryService.calulateGeometricCenter(gpsReceiverPosition, t.getCog() / 10, dimPort, dimStern);

            v.setCenterPosition(geometricCenterPosition);
        }

        return v;
    }


}
