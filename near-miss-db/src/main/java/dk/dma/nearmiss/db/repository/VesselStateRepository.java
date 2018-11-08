package dk.dma.nearmiss.db.repository;

import dk.dma.nearmiss.db.entity.VesselState;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
public class VesselStateRepository {
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
        return vesselState;
    }

}
