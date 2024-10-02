package prueba.chat.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import prueba.chat.model.ChatMessage;
import prueba.chat.service.MessageService;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) throws Exception {
        messageService.saveMessage(chatMessage);  
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage chatMessage) {
        chatMessage.setContent(chatMessage.getSender() + " se ha unido al chat");
        return chatMessage;
    }

    @GetMapping("/chat/start/{recipient}")
    public String startChat(@PathVariable String recipient, Model model, Principal principal) {
        String sender = principal.getName(); 
        model.addAttribute("sender", sender);
        model.addAttribute("recipient", recipient);
        return "chatRoom"; 
    }
}



