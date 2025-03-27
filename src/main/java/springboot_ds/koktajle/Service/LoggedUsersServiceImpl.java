package springboot_ds.koktajle.service;

import org.springframework.stereotype.Service;
import springboot_ds.koktajle.model.User;

import java.util.ArrayList;

@Service
public class LoggedUsersServiceImpl {
    private static final ArrayList<User> loggedUsers = new ArrayList<User>();


    static public  Boolean checkIfUserLoggedIn(User user) {
        Boolean loggedIn = loggedUsers.contains(user);
        return loggedIn;
    }

    static public User returnLoggedUser(String email) {
        for (User loggedUser : loggedUsers) {
            if (loggedUser.getEmail().equals(email)) {
                return loggedUser; // Return immediately if found
            }
        }
        return null; // Return null if no match is found
    }

    public static String logUserIn(User user) {
        if (checkIfUserLoggedIn(user)) {
            return "Uzytkownik juz zalogowany.";
        }
        loggedUsers.add(user);
        return "Pomyślnie zalogowano użytkownika";
    }

    static public String logUserOut(User user) {
        if (checkIfUserLoggedIn(user)) {
            loggedUsers.remove(user);
            return "Pomyślnie wylogowano użytkownika.";
        } else {
            return "Użytkownik nie był zalogowany.";
        }
    }


}
