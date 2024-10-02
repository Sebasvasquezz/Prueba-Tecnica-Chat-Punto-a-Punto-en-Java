package prueba.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ChatApplication is the main entry point for the Spring Boot application.
 * It initializes and runs the application.
 */
@SpringBootApplication
public class ChatApplication {

	 /**
     * The main method that serves as the entry point of the Spring Boot application.
     * It runs the application by calling SpringApplication.run().
     * 
     * @param args Command-line arguments passed to the application (if any).
     */
	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}

}
