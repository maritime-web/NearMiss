package dk.dma.nearmiss.db.repository;

import dk.dma.nearmiss.db.entity.VesselPosition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
public class VesselPositionRepository {
    private final EntityManager em;

    public VesselPositionRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public VesselPosition save(VesselPosition vesselPosition) {
        if (vesselPosition.getId() == null) {
            em.persist(vesselPosition);
        } else {
            em.merge(vesselPosition);
        }
        return vesselPosition;
    }

}
