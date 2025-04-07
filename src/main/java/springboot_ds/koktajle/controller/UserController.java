package springboot_ds.koktajle.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springboot_ds.koktajle.Repository.UserRepository;
import springboot_ds.koktajle.Response.ErrorMessage;
import springboot_ds.koktajle.dto.*;
import springboot_ds.koktajle.model.User;
import springboot_ds.koktajle.service.JwtService;
import springboot_ds.koktajle.service.LoggedUsersServiceImpl;
import springboot_ds.koktajle.service.UserServiceImpl;
import springboot_ds.koktajle.email.EmailService;

import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller

public class UserController {

    // pola wykorzystywane w klasie

    private final static String BASE_URL = "http://localhost:8080"; // to będzie w przyszłości inetaddress
    private final static  long CONFIRMATION_TOKEN_EXPIRATION_TIME = 300000; // 30 sekund na token potwierdzający
    private final static  long LOGIN_TOKEN_EXPIRATION_TIME = 3000000; // 24 godziny na token po zalogowaniu

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final LoggedUsersServiceImpl loggedUsersService;
    private final LoggedUsersServiceImpl loggedUsersServiceImpl;

    public UserController(UserServiceImpl userService, PasswordEncoder passwordEncoder, EmailService emailService, UserRepository userRepository, JwtService jwtService, LoggedUsersServiceImpl loggedUsersServiceImpl) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.loggedUsersService = new LoggedUsersServiceImpl();
        this.loggedUsersServiceImpl = loggedUsersServiceImpl;
    }

    @PostMapping("/api/register")
    public ResponseEntity<UserResponseDto> registerUser(final @RequestBody UserRequestDto userRequestDto) {
        System.out.println("API REGISTER OTRZYMANE");
        // szukamy usera w serwisie - wykorzystanie Optional

        Optional<UserRequestDto> registeredUser = userService.getUserByEmail(userRequestDto.getEmail());

        // jeśli takiego nie ma to go tworzymy, jeśli jest, to odpowiedni response
        if (userRequestDto.getEmail() == null || userRequestDto.getPassword() == null || userRequestDto.getUsername() == null){

        }
        if (registeredUser.isEmpty()) {

            String password = userRequestDto.getPassword();
            System.out.println("Password: "+password);
            System.out.println("Email: "+userRequestDto.getEmail());
            System.out.println("Received request body: " + userRequestDto);
            String encryptedPassword = passwordEncoder.encode(password);
            userRequestDto.setPassword(encryptedPassword);
            userService.createUser(userRequestDto);

            // generujemy token z emaila na czas 30 sekund i odsyłamy do klienta



            String token = jwtService.generateToken(userRequestDto.getEmail(), CONFIRMATION_TOKEN_EXPIRATION_TIME);
            String confirmationLink = BASE_URL + "/api/confirm?token=" + token;
            String response = confirmationLink;
            UserResponseDto responseDto = new UserResponseDto(response);
            ResponseEntity<UserResponseDto> responseObject = new ResponseEntity<UserResponseDto>(responseDto,HttpStatus.OK);
            System.out.println(responseObject);
            emailService.sendEmail(userRequestDto.getEmail(),"POTWIERDZENIE KONTA",confirmationLink);
            return responseObject;

        } else {
            System.out.println("ERROR?");
            UserResponseDto responseDto = new UserResponseDto("UZYTKOWNIK JUZ ISTNIEJE");
            ResponseEntity<UserResponseDto> responseObject = new ResponseEntity<UserResponseDto>(responseDto,HttpStatus.OK);
            return responseObject;
        }

    }

    @GetMapping("/api/confirm")
    public ResponseEntity<?> confirmToken(@RequestParam String token) {
        String email = jwtService.getEmailFromToken(token);
        Date expirationDate = jwtService.getExpirationDateFromToken(token);

        if (expirationDate == null) {
            System.out.println("WYGASL");
            return ResponseEntity.badRequest().body(new ErrorMessage("Token wygasł"));
        }

        Optional<User> DBuser = userRepository.findByEmail(email);

        if (DBuser.isEmpty()) {
            System.out.println("NIE ISTNIEJE TAKI UZYTKOWNIK");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Użytkownik nie istnieje"));
        }

        User user = DBuser.get();
        Boolean isValidToken = jwtService.validateToken(token, user);

        if (!isValidToken) {
            System.out.println("INVALID TOKEN");
            return ResponseEntity.badRequest().body(new ErrorMessage("Token Nie jest Prawdziwy schizolu"));
        }

        user.setConfirmed(true);
        userRepository.save(user);
        System.out.println("JEST DOBRY TOKEN DO CONFIRM.");
        return new ResponseEntity<String>("TEST", HttpStatus.OK);
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(final @Valid @RequestBody UserRequestDto userRequestDto) {

        // jak w register, szukamy usera w serwisie - wykorzystanie Optional z interfejsu
        // jeśli niezarejestrowany, błąd

        Optional<UserRequestDto> registeredUser = userService.getUserByEmail(userRequestDto.getEmail());

        if (registeredUser.isEmpty()) {
            System.out.println("NIE ISTNIEJE TAKI UZYTKOWNIK");
            return new ResponseEntity<>(
                    new TokenResponseDto("TAKI UZYTKOWNIK NIE ISTNIEJE!\nPODANY MAIL: ".concat(userRequestDto.getEmail()),""),
                    HttpStatus.FORBIDDEN);
        } else {



            //jeśli zarejestrowany, sprawdzamy czy się zgadza hasło
            String email = userRequestDto.getEmail();
            Optional<User> DBuser =  userRepository.findByEmail(email);
            if (DBuser.isEmpty()) {
                return new ResponseEntity<>
                        (new TokenResponseDto("TAKI UZYTKOWNIK NIE ISTNIEJE!\nPODANY MAIL: ".concat(userRequestDto.getEmail()),""), HttpStatus.OK);
            }
            String hashedPassword = DBuser.get().getPassword();

            //jeśli nie -> response z HttpStatus.FORBIDDEN
            if (!passwordEncoder.matches(userRequestDto.getPassword(), hashedPassword)) {
                return new ResponseEntity<>(
                        new TokenResponseDto("HASLO SIE NIE ZGADZA.",""),
                        HttpStatus.OK);
            }


            //jeśli niepotwierdzony -> response z HttpStatus.UNAUTHORIZED

            Boolean confirmed = DBuser.get().isConfirmed();
            if (!confirmed) {
                System.out.println("NIE POTWIERDZONE");

                return new ResponseEntity<>(
                        new TokenResponseDto("KONTO JESZCZE NIE POTWIERDZONE.",""),
                        HttpStatus.OK);
            }

            //jeśli dobre hasło i potwierdzony to generujemy token i zwracamy response z tokenem
            //który będzie wykorzystywany podczas autoryzacji get-ów
            //na tym etapie dodajemy klasę TokenResponseDto:
            if (DBuser.isPresent()){
                
            }
            String token = jwtService.generateToken(userRequestDto.getEmail(), LOGIN_TOKEN_EXPIRATION_TIME);

            System.out.println("ZALOGOWANO");
            // to ZAWSZE POWINNO ZADZIALAC, bo user zostal juz wczesniej technicznie ogarniety, teraz tylko w db go szukam.
            User user = userRepository.findByEmail(userRequestDto.getEmail()).get();
            loggedUsersService.logUserIn(user);

            TokenResponseDto responseDto = new TokenResponseDto("Zalogowano poprawnie",token);
            ResponseEntity<TokenResponseDto> responseObject = new ResponseEntity<TokenResponseDto>(responseDto,HttpStatus.OK);
            return responseObject;

        }



    }

    @GetMapping("/api/customlogout")
    public ResponseEntity<?> logoutUser(@RequestParam String token){
        // trzeba sprawdzic czy token podany jest:
        // 1. Prawdziwy i od nas - robi authorization w sumie
        // 2. Nie wygasł już - tak samo robi authorization
        // 3. Pasuje do emaila który chcemy wylogować.
        String email = jwtService.getEmailFromToken(token);
        try {
            User user = userRepository.findByEmail(email).get();

            if (!LoggedUsersServiceImpl.checkIfUserLoggedIn(user)) {
                ResponseEntity<UserResponseDto> responseObject = new ResponseEntity<UserResponseDto>(new UserResponseDto("Nie jestes zalogowany."), HttpStatus.OK);
                return responseObject;
            }

            LoggedUsersServiceImpl.expireToken(token);

            LoggedUsersServiceImpl.logUserOut(user);
            ResponseEntity<UserResponseDto> responseObject = new ResponseEntity<UserResponseDto>(new UserResponseDto("Wylogowano"), HttpStatus.OK);
            return responseObject;
        } catch (NoSuchElementException e){
            e.printStackTrace();
            ResponseEntity<UserResponseDto> responseObject = new ResponseEntity<UserResponseDto>(new UserResponseDto("ERROR: nie ma takiego uzytkownika"), HttpStatus.OK);
            return responseObject;
        }
    }

    @GetMapping("/api/token_check")
    public ResponseEntity<ExpirationCheckRequestDto> tokenCheck(@RequestParam String token){
        if (jwtService.getExpirationDateFromToken(token) == null) {
            ExpirationCheckRequestDto responseObject = new ExpirationCheckRequestDto(true);
            return new ResponseEntity<ExpirationCheckRequestDto>(responseObject,HttpStatus.OK);
        }
        ExpirationCheckRequestDto responseObject = new ExpirationCheckRequestDto(false);
        return new ResponseEntity<ExpirationCheckRequestDto>(responseObject,HttpStatus.OK);
    }
    @GetMapping("/api/user_info")
    public ResponseEntity<InfoRequestDto> userInfo(@RequestParam String token){
        String email = jwtService.getEmailFromToken(token);
        System.out.println("EMAIL: "+email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            System.out.println("EMPTY");
            return new ResponseEntity<InfoRequestDto>(new InfoRequestDto("",""), HttpStatus.OK);
        } else {
            User DBuser = user.get();
            System.out.println("NOT EMPTY");
            return new ResponseEntity<InfoRequestDto>(new InfoRequestDto(DBuser.getUsername(),DBuser.getEmail()),HttpStatus.OK);
        }
    }
}
