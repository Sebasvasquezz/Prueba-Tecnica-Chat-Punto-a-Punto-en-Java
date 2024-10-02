package prueba.chat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import prueba.chat.model.ChatMessage;
import prueba.chat.model.Message;
import prueba.chat.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private MessageService messageService;

    private static final String SENDER = "sender";
    private static final String RECIPIENT = "recipient";
    private static final String PLAIN_TEXT = "Hello, World!";
    private static final String ENCRYPTED_TEXT = "encryptedText";
    private static final String DECRYPTED_TEXT = "Hello, World!";
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveMessageSuccess() throws Exception {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(SENDER);
        chatMessage.setRecipient(RECIPIENT);
        chatMessage.setContent(PLAIN_TEXT);

        when(encryptionService.encrypt(PLAIN_TEXT)).thenReturn(ENCRYPTED_TEXT);

        messageService.saveMessage(chatMessage);

        verify(encryptionService, times(1)).encrypt(PLAIN_TEXT);
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testSaveMessageEncryptionFailure() throws Exception {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(SENDER);
        chatMessage.setRecipient(RECIPIENT);
        chatMessage.setContent(PLAIN_TEXT);

        when(encryptionService.encrypt(PLAIN_TEXT)).thenThrow(new Exception("Encryption error"));

        assertThrows(Exception.class, () -> messageService.saveMessage(chatMessage));

        verify(encryptionService, times(1)).encrypt(PLAIN_TEXT);
        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    void testGetChatHistorySuccess() throws Exception {
        Message message1 = new Message();
        message1.setSender(SENDER);
        message1.setRecipient(RECIPIENT);
        message1.setContent(ENCRYPTED_TEXT);
        message1.setTimestamp(LocalDateTime.now());

        Message message2 = new Message();
        message2.setSender(SENDER);
        message2.setRecipient(RECIPIENT);
        message2.setContent(ENCRYPTED_TEXT);
        message2.setTimestamp(LocalDateTime.now());

        List<Message> messages = Arrays.asList(message1, message2);

        when(messageRepository.findBySenderAndRecipient(SENDER, RECIPIENT)).thenReturn(messages);
        when(encryptionService.decrypt(ENCRYPTED_TEXT)).thenReturn(DECRYPTED_TEXT);

        List<ChatMessage> chatHistory = messageService.getChatHistory(SENDER, RECIPIENT);

        assertEquals(2, chatHistory.size());
        assertEquals(DECRYPTED_TEXT, chatHistory.get(0).getContent());
        assertEquals(DECRYPTED_TEXT, chatHistory.get(1).getContent());

        verify(encryptionService, times(2)).decrypt(ENCRYPTED_TEXT);
        verify(messageRepository, times(1)).findBySenderAndRecipient(SENDER, RECIPIENT);
    }

    @Test
    void testGetChatHistoryDecryptionFailure() throws Exception {
        Message message = new Message();
        message.setSender(SENDER);
        message.setRecipient(RECIPIENT);
        message.setContent(ENCRYPTED_TEXT);
        message.setTimestamp(LocalDateTime.now());

        when(messageRepository.findBySenderAndRecipient(SENDER, RECIPIENT)).thenReturn(Arrays.asList(message));
        when(encryptionService.decrypt(ENCRYPTED_TEXT)).thenThrow(new Exception("Decryption error"));

        assertThrows(RuntimeException.class, () -> messageService.getChatHistory(SENDER, RECIPIENT));

        verify(encryptionService, times(1)).decrypt(ENCRYPTED_TEXT);
        verify(messageRepository, times(1)).findBySenderAndRecipient(SENDER, RECIPIENT);
    }
}
