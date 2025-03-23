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

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        Optional<User> user = this.userRepository.findByEmail(email);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Unknown user " + email);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.get().getEmail())
                .password(user.get().getPassword())
                .build();
    }

}