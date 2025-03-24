package springboot_ds.koktajle.service;

import org.springframework.stereotype.Service;
import springboot_ds.koktajle.model.User;

import java.util.ArrayList;

@Service
public class LoggedUsersServiceImpl  implements LoggedUsersService {
    private final ArrayList<User> loggedUsers;



    public LoggedUsersServiceImpl() {
        loggedUsers = new ArrayList<User>();
    }

    public Boolean checkIfUserLoggedIn(User user) {
        Boolean loggedIn = loggedUsers.contains(user);
        return loggedIn;
    }

    public String logUserIn(User user) {
        if (checkIfUserLoggedIn(user)) {
            return "Uzytkownik juz zalogowany.";
        }
        loggedUsers.add(user);
        return "Pomyślnie zalogowano użytkownika";
    }

    public String logUserOut(User user) {
        if (checkIfUserLoggedIn(user)) {
            loggedUsers.remove(user);
            return "Pomyślnie wylogowano użytkownika.";
        } else {
            return "Użytkownik nie był zalogowany.";
        }
    }


}
