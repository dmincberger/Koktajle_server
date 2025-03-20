package springboot_ds.koktajle.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import springboot_ds.koktajle.Service.JwtService;
import springboot_ds.koktajle.Service.UserServiceImpl;
import springboot_ds.koktajle.dto.TokenResponseDto;
import springboot_ds.koktajle.dto.UserRequestDto;
import springboot_ds.koktajle.dto.UserResponseDto;

import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {

    // pola wykorzystywane w klasie

    private final static String BASE_URL = "http://localhost:8080"; // to będzie w przyszłości inetaddress
    private final static  long CONFIRMATION_TOKEN_EXPIRATION_TIME = 30000; // 30 sekund na token potwierdzający
    private final static  long LOGIN_TOKEN_EXPIRATION_TIME = 86400000; // 24 godziny na token po zalogowaniu

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(){
        this.userService = new UserServiceImpl();
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return "";
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return false;
            }
        };
    }

    @PostMapping("/api/test")
    public ResponseEntity<String> testAnswer(@RequestBody @Valid Map<String,String> requestBody){
        String wiadomosc = requestBody.get("wiadomosc");
        System.out.println(wiadomosc);
        System.out.println("OTRZYMALEM");
        return new ResponseEntity<>("Wiadomosc otrzymana", HttpStatus.ACCEPTED);
    }

    @PostMapping("/api/register")
    public ResponseEntity<UserResponseDto> registerUser(final @RequestBody UserRequestDto userRequestDto) {
        System.out.println("API REGISTER OTRZYMANE");
        // szukamy usera w serwisie - wykorzystanie Optional

        Optional<UserRequestDto> registeredUser = userService.getUserByEmail(userRequestDto.getEmail());

        // jeśli takiego nie ma to go tworzymy, jeśli jest, to odpowiedni response

        if (registeredUser.isEmpty()) {

            String password = userRequestDto.getPassword();
            String encryptedPassword = passwordEncoder.encode(password);
            userRequestDto.setPassword(encryptedPassword);
            userService.createUser(userRequestDto);

            // generujemy token z emaila na czas 30 sekund i odsyłamy do klienta



            String token = JwtService.generateToken(userRequestDto.getEmail(), CONFIRMATION_TOKEN_EXPIRATION_TIME);
            String confirmationLink = BASE_URL + "/api/confirm?token=" + token;
            String response = "AAAAAAAAAAAAAAAAAAAAAAAA";
            UserResponseDto responseDto = new UserResponseDto(confirmationLink);
            ResponseEntity<UserResponseDto> responseObject = new ResponseEntity<UserResponseDto>(responseDto,HttpStatus.OK);
            System.out.println(responseObject);
            return responseObject;

        } else {
            System.out.println("ERROR?");
            UserResponseDto responseDto = new UserResponseDto("UZYTKOWNIK JUZ ISTNIEJE");
            ResponseEntity<UserResponseDto> responseObject = new ResponseEntity<UserResponseDto>(responseDto,HttpStatus.OK);
            return responseObject;
        }

    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(final @Valid @RequestBody UserRequestDto userRequestDto) {

        // jak w register, szukamy usera w serwisie - wykorzystanie Optional z interfejsu
        // jeśli niezarejestrowany, błąd

        Optional<UserRequestDto> registeredUser = userService.getUserByEmail(userRequestDto.getEmail());

        if (registeredUser.isEmpty()) {
            return new ResponseEntity<>(
                    new UserResponseDto("TAKI UZYTKOWNIK NIE ISTNIEJE!\nPODANY MAIL: ".concat(userRequestDto.getEmail())),
                    HttpStatus.FORBIDDEN);
        } else {


            /* tu praca samodzielna:
            jeśli zarejestrowany, sprawdzamy czy się zgadza hasło
            jeśli nie -> response z HttpStatus.FORBIDDEN
            jeśli niepotwierdzony -> response z HttpStatus.UNAUTHORIZED

            jeśli dobre hasło i potwierdzony to generujemy token i zwracamy response z tokenem
            który będzie wykorzystywany podczas autoryzacji get-ów
            na tym etapie dodajemy klasę TokenResponseDto:
            */

            String token = JwtService.generateToken(userRequestDto.getEmail(), LOGIN_TOKEN_EXPIRATION_TIME);

            UserResponseDto responseDto = new UserResponseDto(token);
            ResponseEntity<UserResponseDto> responseObject = new ResponseEntity<UserResponseDto>(responseDto,HttpStatus.OK);
            return responseObject;

        }



    }
    @GetMapping("/api/confirm")
    public ResponseEntity<?> confirmToken(){
        ResponseEntity<String> responseObject = new ResponseEntity<String>("TEST",HttpStatus.OK);
        return responseObject;
     }
}
