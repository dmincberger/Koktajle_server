package springboot_ds.koktajle.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springboot_ds.koktajle.config.EmailOneTimeTokenHandler;
import springboot_ds.koktajle.dto.UserInformationDto;
import springboot_ds.koktajle.service.LoggedUsersServiceImpl;

import java.net.URI;
import java.util.ArrayList;

@Controller
public class AdminController {

    private final EmailOneTimeTokenHandler emailOneTimeTokenHandler;
    public AdminController(EmailOneTimeTokenHandler emailOneTimeTokenHandler) {
        this.emailOneTimeTokenHandler = emailOneTimeTokenHandler;
    }

    @GetMapping("/ott/sent")
    public String ottSent(){
        System.out.println("ottLogin");
        return "success-login";
    }
@PostMapping("/login/ott")
public ResponseEntity<String> ottLogin(){
        System.out.println("ottLogin");
        return ResponseEntity.status(HttpStatus.OK)
            .location(URI.create("/admin/dashboard"))
            .build();  // Redirect to admin dashboard
}
@GetMapping("/admin/login")
    public String adminLogin(){
        System.out.println("admin-login");
        return "admin-login";
}
@GetMapping("/admin/dashboard")
        public String adminPanel(Model model) {
        System.out.println("admin-dashboard");
    ArrayList<UserInformationDto> loggedUsers = LoggedUsersServiceImpl.returnLoggedUsersInfo();
        model.addAttribute("users",loggedUsers);
        return "admin-panel";
    }
}
