package dk.dma.nearmiss.db.repository;

import dk.dma.nearmiss.db.entity.VesselState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.OffsetDateTime;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class VesselStateRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EntityManager em;

    public VesselStateRepository(EntityManager em) {
        this.em = em;
    }

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

    public List<VesselState> list(OffsetDateTime from, OffsetDateTime to, boolean onlyNearMissStates) {
        String baseSql = "SELECT vs FROM VesselState vs ";
        String criteria1 = " vs.positionTime BETWEEN :from AND :to ";
        String criteria2 = " vs.latitude  != 'NaN' ";
        String criteria3 = " vs.longitude  != 'NaN' ";
        String nearMissCriteria = " vs.isNearMiss = TRUE ";
        String queryString;

        if (Boolean.TRUE.equals(onlyNearMissStates)) {
            queryString = String.format("%s WHERE %s AND %s AND %s AND %s",
                    baseSql, criteria1, criteria2, criteria3, nearMissCriteria);
        } else {
            queryString = String.format("%s WHERE %s AND %s AND %s",
                    baseSql, criteria1, criteria2, criteria3);
        }

        TypedQuery<VesselState> query = em.createQuery(queryString, VesselState.class);
        query.setParameter("from", from.toLocalDateTime());
        query.setParameter("to", to.toLocalDateTime());
        List<VesselState> result = query.getResultList();
        logger.debug(String.format("Number of VesselState records found: %s", result.size()));
        return result;
    }

}
