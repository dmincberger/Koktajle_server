package springboot_ds.koktajle.Service;

import springboot_ds.koktajle.Repository.UserRepository;
import springboot_ds.koktajle.dto.UserRequestDto;

import java.util.ArrayList;
import java.util.Optional;

public class UserServiceImpl implements UserService{
    private final UserRepository userRepository; // repository w którym jest lista na userów i działania na niej
    private final ArrayList<String> expiredTokens = new ArrayList<>(); // lista na wygasłe tokeny

    public UserServiceImpl(){
        this.userRepository = new UserRepository();
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserRequestDto user) {
        userRepository.addUser(user);
    }

    public void addExpiredToken(String token) {
        expiredTokens.add(token);
    }

    public Boolean expiredTokensContains(String token) {
        return expiredTokens.contains(token);
    }

    public Optional<UserRequestDto> getUserByEmail(String email) {
        Optional<UserRequestDto> optionaluser = userRepository.findUserByEmail(email);
        return optionaluser;
    }
}
