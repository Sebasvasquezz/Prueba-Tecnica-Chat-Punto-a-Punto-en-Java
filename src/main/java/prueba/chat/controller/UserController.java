package prueba.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import prueba.chat.model.User;
import prueba.chat.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

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

    @GetMapping("/all")
    public String getAllUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users); 
        return "userList";
    }
}

