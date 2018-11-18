package dk.dma.nearmiss.web;

import dk.dma.nearmiss.db.entity.VesselState;
import dk.dma.nearmiss.db.repository.VesselStateRepository;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GpxController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final VesselStateRepository repository;

    public GpxController(VesselStateRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/gpx")
    public String gpx(Integer mmsi, String from, String to, Boolean onlyNearMissStates) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        final OffsetDateTime fromOffset = OffsetDateTime.parse(from, formatter);
        final OffsetDateTime toOffset = OffsetDateTime.parse(to, formatter);

        logger.debug("mmsi: {}", mmsi);
        logger.debug("from: {}", from);
        logger.debug("to: {}", to);
        logger.debug("onlyNearMissStates: {}", onlyNearMissStates);

        final List<VesselState> entities = repository.listByMmsi(mmsi, fromOffset, toOffset, false);
        final List<WayPoint> wayPoints = entities.stream().map(e -> new GpxWaypointConverter(e).convert()).collect(Collectors.toList());

        final GPX gpx = GPX.builder().addTrack(track -> track.addSegment(segment -> segment.points(wayPoints).build())).build();
        return GPX.writer().toString(gpx);
    }
}
