package dk.dma.nearmiss.db.entity;

import javax.persistence.*;

@SuppressWarnings("JpaQlInspection")
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "query_newest_messages", query = "select m from Message m order by m.id desc")
})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String message;

    @SuppressWarnings("unused")
    protected Message() {
    }

    public Message(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

}
