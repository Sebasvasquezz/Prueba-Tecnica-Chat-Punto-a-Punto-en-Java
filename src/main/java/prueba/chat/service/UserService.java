package prueba.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import prueba.chat.model.User;
import prueba.chat.repository.UserRepository;

import java.util.List;


/**
 * UserService handles the business logic related to user management, such as user registration,
 * finding users by username, and retrieving a list of all users.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user by encoding their password and saving the user entity to the database.
     * 
     * @param username The username of the new user.
     * @param password The plain text password of the new user.
     * @return The newly registered User entity.
     */
    public User registerUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); 
        return userRepository.save(user);
    }

    /**
     * Finds a user by their username.
     * 
     * @param username The username to search for.
     * @return The User entity matching the given username, or null if no user is found.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Retrieves a list of all registered users.
     * 
     * @return A list of all User entities.
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}

