package prueba.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import prueba.chat.model.User;
import prueba.chat.repository.UserRepository;

/**
 * DataInitializer is a Spring component that implements CommandLineRunner to run 
 * initialization logic after the application starts. It is used to insert default 
 * data into the database if needed.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This method is executed automatically after the application starts.
     * It checks if there are any users in the database, and if not, creates a default admin user.
     * 
     * @param args Command-line arguments (not used in this case).
     * @throws Exception If an error occurs during user creation.
     */
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Encode the admin password
            
            userRepository.save(admin);

            System.out.println("Admin user created with username 'admin' and password 'admin123'");
        }
    }
}
