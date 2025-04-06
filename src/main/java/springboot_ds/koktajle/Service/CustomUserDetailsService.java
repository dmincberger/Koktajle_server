package springboot_ds.koktajle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springboot_ds.koktajle.Repository.UserRepository;
import springboot_ds.koktajle.dto.UserRequestDto;
import springboot_ds.koktajle.model.User;

import java.util.Optional;

@Service

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loadDatabaseUserByUsername(String Username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(Username);
        if (user.isPresent()) {
            return user.get();

        } else {
            throw new UsernameNotFoundException(Username);
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        Optional<User> user = this.userRepository.findByUsername(username);

        if(user.isEmpty()) {
            Optional<User> email_user = this.userRepository.findByEmail(username);
            if (email_user.isPresent()) {
                return email_user.get();
            }
            throw new UsernameNotFoundException("Unknown user: username -  " + username);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.get().getEmail())
                .password(user.get().getPassword())
                .build();
    }

}