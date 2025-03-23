package springboot_ds.koktajle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot_ds.koktajle.Repository.UserRepository;
import springboot_ds.koktajle.dto.UserRequestDto;
import springboot_ds.koktajle.model.User;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements springboot_ds.koktajle.service.UserService {

    private final UserRepository userRepository; // repository w którym jest lista na userów i działania na niej
    private final ArrayList<String> expiredTokens = new ArrayList<>(); // lista na wygasłe tokeny

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserRequestDto user) {
        User DBuser = new User();
        DBuser.setEmail(user.getEmail());
        DBuser.setPassword(user.getPassword());
        DBuser.setUsername(user.getUsername());
        userRepository.save(DBuser);
    }

    public void addExpiredToken(String token) {
        expiredTokens.add(token);
    }

    public Boolean expiredTokensContains(String token) {
        return expiredTokens.contains(token);
    }

    public Optional<UserRequestDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email) // Returns Optional<User>
                .map(user -> new UserRequestDto(user.getEmail(), user.getPassword(),user.getUsername()));
    }
}
