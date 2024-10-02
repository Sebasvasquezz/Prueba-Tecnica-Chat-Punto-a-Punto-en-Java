package prueba.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import prueba.chat.model.User;

/**
 * UserRepository is a JPA repository interface for performing CRUD operations on the User entity.
 * It also provides a method to retrieve a user by their username.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their unique username.
     * 
     * @param username The username of the user to find.
     * @return The User entity that matches the given username, or null if no match is found.
     */
    User findByUsername(String username);
}

