package springboot_ds.koktajle.service;

import springboot_ds.koktajle.model.User;

public interface LoggedUsersService {
    String logUserIn(User user);
    String logUserOut(User user);
    Boolean checkIfUserLoggedIn(User user);
}
