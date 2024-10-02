package prueba.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import prueba.chat.model.User;

/**
 * LoginController handles requests related to user login and registration.
 * It is responsible for showing the login page and the registration form.
 */
@Controller
public class LoginController {

    /**
     * Handles GET requests to "/login" and returns the login page view.
     * This method does not require any authentication and is publicly accessible.
     * 
     * @return The name of the view ("login") that will render the login page.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  
    }

    /**
     * Handles GET requests to "/register" and returns the registration form.
     * This method adds a new, empty User object to the model so that it can be populated by the form.
     * 
     * @param model The Model object used to pass data to the view.
     * @return The name of the view ("register") that will render the registration form.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); 
        return "register"; 
    }       
}
