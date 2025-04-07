package springboot_ds.koktajle.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import springboot_ds.koktajle.model.User;
import springboot_ds.koktajle.service.CustomUserDetailsService;

import java.io.IOException;

@Component
public class CustomOTTchecker extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;

    public CustomOTTchecker(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if ("/ott/generate".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String username = request.getParameter("username");
            User user = userDetailsService.loadDatabaseUserByUsername(username);

            if (!user.getEmail().equals("javamail674@gmail.com")) {
                response.sendRedirect("/login?error=bad-email");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
