package prueba.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prueba.chat.model.ChatMessage;
import prueba.chat.model.Message;
import prueba.chat.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EncryptionService encryptionService;

    public void saveMessage(ChatMessage chatMessage) throws Exception {
        String encryptedContent = encryptionService.encrypt(chatMessage.getContent());
        Message message = new Message();
        message.setSender(chatMessage.getSender());
        message.setRecipient(chatMessage.getRecipient());
        message.setContent(encryptedContent);  
        message.setTimestamp(LocalDateTime.now());  
        messageRepository.save(message);
    }

    public List<ChatMessage> getChatHistory(String sender, String recipient) throws Exception {
        List<Message> messages = messageRepository.findBySenderAndRecipient(sender, recipient);

        return messages.stream().map(msg -> {
            try {
                String decryptedContent = encryptionService.decrypt(msg.getContent());
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setSender(msg.getSender());
                chatMessage.setRecipient(msg.getRecipient());
                chatMessage.setContent(decryptedContent);  
                return chatMessage;
            } catch (Exception e) {
                throw new RuntimeException("Error al descifrar el mensaje", e);
            }
        }).collect(Collectors.toList());
    }
}



