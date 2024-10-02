package prueba.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import prueba.chat.model.Message;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndRecipient(String sender, String recipient);
    List<Message> findBySender(String sender);
    List<Message> findByRecipient(String recipient);
}

