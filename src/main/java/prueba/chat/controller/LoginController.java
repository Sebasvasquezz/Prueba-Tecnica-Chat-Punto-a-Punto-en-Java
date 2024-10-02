package prueba.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import prueba.chat.model.User;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); 
        return "register"; 
    }       
}
