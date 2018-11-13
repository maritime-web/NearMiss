package dk.dma.nearmiss.engine.engineParts;

import dk.dma.nearmiss.engine.Vessel;
import dk.dma.nearmiss.engine.geometry.GeometryService;
import dk.dma.nearmiss.helper.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EllipseShapedSafetyZoneDetectorTest {

    @Mock
    private Vessel ownVessel;

    @Mock
    private Vessel otherVessel;

    private GeometryService geometryService = new GeometryService();

    private EllipseShapedSafetyZoneDetector sut; // http://xunitpatterns.com/SUT.html

    @Before
    public void before() {
        when(ownVessel.getCenterPosition()).thenReturn(new Position(55.0, 10.0));
        when(ownVessel.getLoa()).thenReturn(100);
        when(ownVessel.getBeam()).thenReturn(25);
        when(ownVessel.getCog()).thenReturn(90.0);
        when(ownVessel.getSog()).thenReturn(10.0);

        when(otherVessel.getCenterPosition()).thenReturn(new Position(54.0,9.0));
        when(otherVessel.getLoa()).thenReturn(50);
        when(otherVessel.getBeam()).thenReturn(15);
        when(otherVessel.getHdg()).thenReturn(90.0);

        sut = new EllipseShapedSafetyZoneDetector(geometryService, ownVessel);
    }

    @Test
    public void detectsNearMissWithSelf() {
        assertTrue(sut.nearMissDetected(ownVessel));
    }

    @Test
    public void detectsNearMissWithOtherVesselCloseBy() {
        when(otherVessel.getCenterPosition()).thenReturn(new Position(55.0001, 10.0001));
        assertTrue(sut.nearMissDetected(otherVessel));
    }

    @Test
    public void detectsNoNearMissWithOtherVesselFarAway() {
        assertFalse(sut.nearMissDetected(otherVessel));
    }

}