package springboot_ds.koktajle.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SecurityConfigs {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login","/register","/css/**").permitAll()
                .anyRequest().authenticated())
                .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("login")
                .passwordParameter("password")
                // .defaultSuccessUrl("/index")
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    System.out.println("logged in");
                    response.sendRedirect("/index");
                })
        )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            System.out.println("logged out");
                            response.sendRedirect("/login");

                        })
                ).build();

    }

}
