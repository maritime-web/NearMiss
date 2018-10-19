package dk.dma.nearmiss.db.repository;

import dk.dma.nearmiss.db.NearMissDbApplication;
import dk.dma.nearmiss.db.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NearMissDbApplication.class)
public class MessageRepositoryTests {

    @Autowired
    MessageRepository repository;

    @Test
    public void findById() {
        Message message = new Message("Message1");
        Message savedMessage = repository.save(message);
        Message foundMessage = repository.findById(savedMessage.getId());
        assertEquals("expected saved message found", message.getMessage(), foundMessage.getMessage());
    }

    @Test
    public void listNewest() {
        for (int i = 0; i < 12; i++) {
            repository.save(new Message(String.format("Message[%s]", i)));
        }
        List<Message> newest10Messages = repository.listNewest();
        assertEquals("expected result with 10 messages", 10, newest10Messages.size());
        assertEquals("first message to be newest", "Message[11]", newest10Messages.get(0).getMessage());
    }

}
