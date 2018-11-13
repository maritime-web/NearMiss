package dk.dma.nearmiss.db;

import dk.dma.nearmiss.db.entity.VesselState;
import dk.dma.nearmiss.db.repository.VesselStateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NearMissDbApplication.class)
public class VesselStateRepositoryTests {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    @Autowired
    VesselStateRepository repository;

    @Test
    public void list() {
        OffsetDateTime from = OffsetDateTime.parse("2018-11-09T12:34:12.673Z", FORMATTER);
        OffsetDateTime to = OffsetDateTime.parse("2018-11-09T12:34:12.673Z", FORMATTER);
        List<VesselState> result = repository.list(from, to, false);
        assertNotNull("Expected to receive result", result);
    }

}
