package dk.dma.nearmiss.db.repository;

import dk.dma.nearmiss.db.entity.Message;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("unused")
@Repository
public class MessageRepository {
    private final EntityManager em;

    public MessageRepository(EntityManager em) {
        this.em = em;
    }

    @SuppressWarnings("WeakerAccess")
    public Message findById(Long id) {
        return em.find(Message.class, id);
    }

    @Transactional
    public Message save(Message message) {
        if (message.getId() == null) {
            em.persist(message);
        } else {
            em.merge(message);
        }
        return message;
    }

    public List<Message> listNewest() {
        @SuppressWarnings("JpaQueryApiInspection")
        TypedQuery<Message> query = em.createNamedQuery("query_newest_messages", Message.class);
        query.setMaxResults(10);
        return query.getResultList();
    }
}
