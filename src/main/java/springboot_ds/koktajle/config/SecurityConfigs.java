package springboot_ds.koktajle.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import springboot_ds.koktajle.config.EmailOneTimeTokenHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableWebSecurity
public class SecurityConfigs {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, EmailOneTimeTokenHandler emailOneTimeTokenHandler) throws Exception {
        http
                //csrf
                .csrf(csrf -> csrf.disable())
                //requests
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/register/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/confirm").permitAll()
                        .anyRequest().authenticated()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    };

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
