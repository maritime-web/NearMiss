package dk.dma.nearmiss.db.repository;

import dk.dma.nearmiss.db.entity.VesselState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class VesselStateRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EntityManager em;

    public VesselStateRepository(EntityManager em) {
        this.em = em;
    }

    @SuppressWarnings("UnusedReturnValue")
    @Transactional
    public VesselState save(VesselState vesselState) {
        if (vesselState.getId() == null) {
            em.persist(vesselState);
        } else {
            em.merge(vesselState);
        }
        logger.debug(String.format("Saved: %s", vesselState));
        return vesselState;
    }

    @SuppressWarnings("JpaQueryApiInspection")
    public List<VesselState> list(OffsetDateTime from, OffsetDateTime to, boolean onlyNearMissStates) {
        Boolean[] nearMissStates = onlyNearMissStates ? new Boolean[]{Boolean.TRUE} : new Boolean[]{Boolean.TRUE, Boolean.FALSE};
        TypedQuery<VesselState> query = em.createNamedQuery("listVesselStates", VesselState.class);
        query.setParameter("from", from.toLocalDateTime());
        query.setParameter("to", to.toLocalDateTime());
        query.setParameter("nearMissStates", Arrays.asList(nearMissStates));

        List<VesselState> result = query.getResultList();
        logger.debug(String.format("Number of VesselState records found: %s", result.size()));
        return result;
    }

}
