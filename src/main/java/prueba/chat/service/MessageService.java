package prueba.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prueba.chat.model.ChatMessage;
import prueba.chat.model.Message;
import prueba.chat.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MessageService handles the saving and retrieval of chat messages.
 * It also provides encryption and decryption of message content using the EncryptionService.
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EncryptionService encryptionService;


    /**
     * Saves a chat message to the database after encrypting its content.
     * 
     * @param chatMessage The chat message to be saved.
     * @throws Exception If encryption fails or any other error occurs during the saving process.
     */
    public void saveMessage(ChatMessage chatMessage) throws Exception {
        String encryptedContent = encryptionService.encrypt(chatMessage.getContent());
        Message message = new Message();
        message.setSender(chatMessage.getSender());
        message.setRecipient(chatMessage.getRecipient());
        message.setContent(encryptedContent);  
        message.setTimestamp(LocalDateTime.now());  
        messageRepository.save(message);
    }

        /**
     * Retrieves the chat history between a sender and a recipient from the database.
     * The content of each message is decrypted before returning it.
     * 
     * @param sender The username of the sender.
     * @param recipient The username of the recipient.
     * @return A list of decrypted chat messages exchanged between the sender and recipient.
     * @throws Exception If decryption fails or any other error occurs during the retrieval process.
     */
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



