package prueba.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import prueba.chat.model.User;
import prueba.chat.service.UserService;

import java.util.List;

/**
 * UserController handles requests related to user management, including user registration
 * and retrieving the list of all registered users.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Handles POST requests to register a new user.
     * It attempts to register the user with the provided username and password.
     * If successful, the user is redirected to the login page. If there is an error, 
     * the registration form is re-displayed with an error message.
     * 
     * @param user The User object containing the registration details (username, password).
     * @param model The Model object used to pass data to the view.
     * @return The name of the view to be rendered (either "login" on success or "register" on failure).
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
    try {
        userService.registerUser(user.getUsername(), user.getPassword());
        model.addAttribute("message", "User registered successfully!");
        model.addAttribute("messageType", "success");
    } catch (Exception e) {
        model.addAttribute("message", "Error registering user: " + e.getMessage());
        model.addAttribute("messageType", "error");
        return "register"; 
    }
    return "login";  
    }

    /**
     * Handles GET requests to retrieve and display all registered users.
     * It fetches the list of all users and adds it to the model for display.
     * 
     * @param model The Model object used to pass data to the view.
     * @return The name of the view ("userList") that will display the list of users.
     */
    @GetMapping("/all")
    public String getAllUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users); 
        return "userList";
    }
}

