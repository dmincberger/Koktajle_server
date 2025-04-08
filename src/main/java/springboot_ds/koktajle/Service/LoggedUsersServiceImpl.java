package springboot_ds.koktajle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot_ds.koktajle.Repository.UserRepository;
import springboot_ds.koktajle.dto.UserInformationDto;
import springboot_ds.koktajle.model.User;

import java.util.ArrayList;

@Service
public class LoggedUsersServiceImpl {
    private static final ArrayList<User> loggedUsers = new ArrayList<User>();
    private static final ArrayList<String> expiredTokens = new ArrayList<String>();



    static public Boolean checkIfUserLoggedIn(String email) {
        for (User user : loggedUsers) {
            if (user.getEmail().equals(email)) {
                System.out.println("Logged in user: " + user.toString());
                return true;
            }
        }
        return false;
    }

    static public User returnLoggedUser(String email) {
        for (User loggedUser : loggedUsers) {
            if (loggedUser.getEmail().equals(email)) {
                return loggedUser; // Return immediately if found
            }
        }
        return null; // Return null if no match is found
    }

    public static String logUserIn(String email, User user) {
        if (checkIfUserLoggedIn(email)) {
            return "Uzytkownik juz zalogowany.";
        }
        loggedUsers.add(user);
        return "Pomyślnie zalogowano użytkownika";
    }

    static public Boolean logUserOut(String email, User user) {
        if (checkIfUserLoggedIn(email)) {
            for (User loggedUser : loggedUsers) {
                if (loggedUser.getEmail().equals(email)) {
                    loggedUsers.remove(loggedUser);
                    System.out.println("Logged out user: " + user.toString());
                    return true;
                }
            }
        } else {
            System.out.println("Uzytkownik nigdy nie byl zalogowany.");
            return false;
        }
        System.out.println("Jakims cudem, uzytkownik jest zalogowany, i nie jest rownoczesnie");
        return false;
    }
    static public ArrayList<UserInformationDto> returnLoggedUsersInfo(){
        ArrayList<UserInformationDto>usersInfo = new ArrayList<>();
        for (User loggeduser: loggedUsers) {
            usersInfo.add(new UserInformationDto(loggeduser.getUsername(),loggeduser.getEmail(),loggeduser.isConfirmed()));
        }
        return usersInfo;
    }
    static public void expireToken(String token){
        expiredTokens.add(token);
    }
    static public Boolean isTokenExpired(String token){
        if (expiredTokens.contains(token)){
            return true;
        }
        return false;
    }
}
