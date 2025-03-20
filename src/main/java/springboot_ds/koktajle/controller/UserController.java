package springboot_ds.koktajle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @PostMapping("/register")
    public String register() {
        return "placeholder";
    }

    @PostMapping("/login")
    public String login() {
        return "placeholder";
    }

    @PostMapping("/logout")
    public String logout() {
        return "placeholder";
    }

}
