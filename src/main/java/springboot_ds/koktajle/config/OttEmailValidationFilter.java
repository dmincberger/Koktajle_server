package springboot_ds.koktajle.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import springboot_ds.koktajle.service.CustomUserDetailsService;

import java.io.IOException;

@Component
public class OttEmailValidationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;

    public OttEmailValidationFilter(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if ("/ott/generate".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String username = request.getParameter("username");
            if (username != null) {
                String email = userDetailsService.loadDatabaseUserByUsername(username).getEmail();

                if (!email.equals("admin@example.com")) {
                    System.out.println("Blocked email: " + email);
                    response.sendRedirect("/login?error=not-allowed");
                    return; // 🚫 Stop here
                }
            }
        }

        filterChain.doFilter(request, response); // ✅ Proceed normally
    }
}
