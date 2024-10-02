package prueba.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import prueba.chat.model.ChatMessage;
import prueba.chat.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public void sendMessage(@RequestBody ChatMessage message) throws Exception {
        System.out.println("Se mando un mensaje en MessageController");
        messageService.saveMessage(message);
    }

    @GetMapping("/history/{sender}/{recipient}")
    public List<ChatMessage> getChatHistory(@PathVariable String sender, @PathVariable String recipient) throws Exception {
        return messageService.getChatHistory(sender, recipient);
    }
}


