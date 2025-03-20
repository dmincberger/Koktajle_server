package springboot_ds.koktajle.Repository;

import org.springframework.stereotype.Repository;
import springboot_ds.koktajle.dto.UserRequestDto;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepository {
    private ArrayList<UserRequestDto> userRepository;

    public UserRepository(){
        userRepository = new ArrayList<>();
    }

    public void addUser(UserRequestDto user){
        userRepository.add(user);
    }

    public Optional<UserRequestDto> findUserByEmail(String email){
        try {
            for (UserRequestDto user : userRepository) {
                if (Objects.equals(user.getEmail(), email)){
                    Optional<UserRequestDto> userOptional = Optional.ofNullable(user);
                    return userOptional;
                }
            }

            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // jak rozroznic error od braku uzytkownika?
        }
    }
}
