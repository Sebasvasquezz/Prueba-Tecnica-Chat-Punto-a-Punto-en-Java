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


/**
 * ChatController handles chat-related requests, including WebSocket messaging 
 * and rendering chat rooms. It also integrates with the MessageService for saving messages.
 */
@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    /**
     * Handles incoming chat messages sent to the "/chat.sendMessage" endpoint.
     * The message is saved using the MessageService and then sent to the "/topic/public" 
     * destination, which broadcasts it to all subscribed clients in the public chat room.
     * 
     * @param chatMessage The message sent by the client.
     * @return The same ChatMessage object after being processed and saved.
     * @throws Exception If an error occurs while processing the message.
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) throws Exception {
        messageService.saveMessage(chatMessage);  
        return chatMessage;
    }

    /**
     * Handles the request to start a new chat between the current user (sender) and the recipient.
     * This method sets up the chat room view by passing the sender and recipient details to the model.
     * 
     * @param recipient The username of the recipient.
     * @param model The Model object used to pass data to the view.
     * @param principal The currently authenticated user's details, used to retrieve the sender's username.
     * @return The name of the view ("chatRoom") to render the chat interface.
     */
    @GetMapping("/chat/start/{recipient}")
    public String startChat(@PathVariable String recipient, Model model, Principal principal) {
        String sender = principal.getName(); 
        model.addAttribute("sender", sender);
        model.addAttribute("recipient", recipient);
        return "chatRoom"; 
    }
}



