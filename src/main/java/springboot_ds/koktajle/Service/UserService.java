package springboot_ds.koktajle.Service;

import springboot_ds.koktajle.dto.UserRequestDto;

import java.util.Optional;

public interface UserService {
    public void createUser(UserRequestDto user);
    public void addExpiredToken(String token);
    public Boolean expiredTokensContains(String token);
    public Optional<UserRequestDto> getUserByEmail(String email);

}
