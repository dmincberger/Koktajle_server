package springboot_ds.koktajle.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import springboot_ds.koktajle.model.User;
import springboot_ds.koktajle.service.JwtService;
import springboot_ds.koktajle.service.LoggedUsersServiceImpl;

import java.io.IOException;
import java.util.Optional;

@Component

public class JwtAuthorizationFilter extends OncePerRequestFilter {


    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public JwtAuthorizationFilter(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // szukamy nagłowka Authorization
        System.out.println("Czy ty to nawet sprawdzasz");
        String authHeader = request.getHeader("Authorization");

        // jeśli jest i zaczyna się od Bearer

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // pobieramy token i sprawdzamy czy zawiera email

            String token = authHeader.substring(7);
            String email = jwtService.getEmailFromToken(token);
            System.out.println(token);
            // jeśli tak
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            User LoggedUser = LoggedUsersServiceImpl.returnLoggedUser(email);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null && LoggedUser != null) {

                // to SpringSecurity sprawdza czy nasz user istnieje


                // i czy token jest ważny

                if (jwtService.validateToken(token, LoggedUser)) {

                    // jeśli wszystko sie zgadza, Security pozwala na żądanie czyli dopuszcza do kontrolera

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }

        }
        filterChain.doFilter(request, response);
    }
}