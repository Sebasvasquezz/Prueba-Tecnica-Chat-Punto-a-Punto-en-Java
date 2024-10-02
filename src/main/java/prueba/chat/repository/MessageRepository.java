package prueba.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import prueba.chat.model.Message;
import java.util.List;

/**
 * MessageRepository is a JPA repository interface for performing CRUD operations on the Message entity.
 * It provides additional methods to retrieve messages based on the sender and recipient.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Finds all messages exchanged between a specific sender and recipient.
     *
     * @param sender The username of the sender.
     * @param recipient The username of the recipient.
     * @return A list of messages exchanged between the sender and recipient.
     */
    List<Message> findBySenderAndRecipient(String sender, String recipient);
    
    /**
     * Finds all messages sent by a specific sender.
     *
     * @param sender The username of the sender.
     * @return A list of messages sent by the specified sender.
     */
    List<Message> findBySender(String sender);

    /**
     * Finds all messages received by a specific recipient.
     *
     * @param recipient The username of the recipient.
     * @return A list of messages received by the specified recipient.
     */
    List<Message> findByRecipient(String recipient);
}

