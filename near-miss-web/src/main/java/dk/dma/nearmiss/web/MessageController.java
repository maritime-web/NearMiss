package dk.dma.nearmiss.web;

import dk.dma.nearmiss.db.entity.Message;
import dk.dma.nearmiss.db.repository.MessageRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {
    private MessageRepository repository;

    public MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/messages")
    public List<Message> getHotels() {
        return repository.listNewest();
    }
}
