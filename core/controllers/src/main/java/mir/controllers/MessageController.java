package mir.controllers;


import mir.models.ParsedMessage;
import mir.services.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/message")
public class MessageController {

    private final IMessageService service;

    @Autowired
    public MessageController(IMessageService service) {
        this.service = service;
    }

    @GetMapping
    public List<ParsedMessage> getMessages() {
        return service.getMessages();
    }

    @PostMapping
    public void addMessage(@RequestBody ParsedMessage parsedMessage) {
        service.addMessage(parsedMessage);
    }

    @DeleteMapping(path = "{id}")
    public void deleteMessage(@PathVariable("id") Integer id) {
        service.deleteMessageById(id);
    }

    @PutMapping(path = "{id}")
    public void updateMessage(@PathVariable("id") Integer id,
                              @RequestParam(required = false) String text) {
        service.updateMessage(id, text);
    }
}
