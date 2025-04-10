package springboot_ds.koktajle.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springboot_ds.koktajle.config.EmailOneTimeTokenHandler;
import springboot_ds.koktajle.dto.AdminRequestDto;
import springboot_ds.koktajle.dto.UserInformationDto;
import springboot_ds.koktajle.dto.UserRequestDto;
import springboot_ds.koktajle.dto.UserResponseDto;
import springboot_ds.koktajle.service.LoggedUsersServiceImpl;
import springboot_ds.koktajle.service.UserServiceImpl;

import java.net.URI;
import java.util.ArrayList;

@Controller
public class AdminController {

    private final EmailOneTimeTokenHandler emailOneTimeTokenHandler;
    @Autowired
    private final UserServiceImpl userServiceImpl;
    public AdminController(EmailOneTimeTokenHandler emailOneTimeTokenHandler, UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder) {
        this.emailOneTimeTokenHandler = emailOneTimeTokenHandler;
        this.userServiceImpl = userServiceImpl;
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
    ArrayList<UserInformationDto> allUsers = userServiceImpl.getUsers();
        model.addAttribute("loggedusers",loggedUsers);
        model.addAttribute("allusers",allUsers);
        return "admin-panel";
    }
@PostMapping("/admin/add")
    public ResponseEntity<UserResponseDto> addUser(@RequestBody AdminRequestDto adminRequestDto){
        System.out.println("addUser");
        String email = adminRequestDto.getEmail();
        String username = adminRequestDto.getUsername();
        if (userServiceImpl.getUserByEmail(email).isPresent() || userServiceImpl.getUserByUsername(username).isPresent()){
            System.out.println("user already exists");
            return new ResponseEntity<UserResponseDto>(new UserResponseDto("Uzytkownik juz istnieje o takim emailu lub username"),HttpStatus.OK);
        } else {
            System.out.println("user added");
        userServiceImpl.adminCreateUser(adminRequestDto);
        return new ResponseEntity<UserResponseDto>(new UserResponseDto("Stworzono uzytkownika"),HttpStatus.OK);
}
    }
}
