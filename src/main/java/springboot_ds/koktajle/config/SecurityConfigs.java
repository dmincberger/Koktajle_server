package springboot_ds.koktajle.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springboot_ds.koktajle.auth.JwtAuthorizationFilter;

import java.util.List;

@Configuration
public class SecurityConfigs {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final UserDetailsService userDetailsService;
    @Autowired
    private final OttEmailValidationFilter ottEmailValidationFilter;
    public SecurityConfigs(JwtAuthorizationFilter jwtAuthorizationFilter, UserDetailsService userDetailsService, OttEmailValidationFilter ottEmailValidationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.userDetailsService = userDetailsService;
        this.ottEmailValidationFilter = ottEmailValidationFilter;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, EmailOneTimeTokenHandler emailOneTimeTokenHandler, OttEmailValidationFilter ottEmailValidationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/register/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/confirm/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/admin/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/login/ott").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customlogout").permitAll()
                        .anyRequest().authenticated()
                ).formLogin(Customizer.withDefaults())
                .addFilterBefore(ottEmailValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .oneTimeTokenLogin(configurer -> configurer.tokenGenerationSuccessHandler(emailOneTimeTokenHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class); // ⬅️ Add JWT Filter Here

        return http.build();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(List.of(authProvider));
    }
}