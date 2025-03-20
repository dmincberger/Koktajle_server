package springboot_ds.koktajle.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springboot_ds.koktajle.Repository.UserRepository;
import springboot_ds.koktajle.dto.UserRequestDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailsService(){
        this.repository = new UserRepository();
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        Optional<UserRequestDto> user = this.repository.findUserByEmail(email);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Unknown user " + email);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.get().getEmail())
                .password(user.get().getPassword())
                .build();
    }

}