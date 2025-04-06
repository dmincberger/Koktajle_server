package springboot_ds.koktajle.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import springboot_ds.koktajle.email.EmailService;
import springboot_ds.koktajle.model.User;
import springboot_ds.koktajle.service.CustomUserDetailsService;

import java.io.IOException;

@Component
public class EmailOneTimeTokenHandler implements OneTimeTokenGenerationSuccessHandler {

    private final EmailService emailService;
    private final CustomUserDetailsService userDetailsService;
    private final OneTimeTokenGenerationSuccessHandler redirectHandler
            = new RedirectOneTimeTokenGenerationSuccessHandler("/ott/sent");

    public EmailOneTimeTokenHandler(EmailService emailService, CustomUserDetailsService userDetailsService) {
        this.emailService = emailService;
        this.userDetailsService = userDetailsService;
    }

    private String getUserEmail(String username) {

        User user = userDetailsService.loadDatabaseUserByUsername(username);
        return user.getEmail();
    }



    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       OneTimeToken oneTimeToken) throws IOException, ServletException {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(UrlUtils.buildFullRequestUrl(request))
                .replacePath(request.getContextPath())
                .replaceQuery(null)
                .fragment(null)
                .path("/login/ott")
                .queryParam("token", oneTimeToken.getTokenValue());

        String link = builder.toUriString();
        System.out.println("link: "+ link);
        String email = getUserEmail(oneTimeToken.getUsername());

        this.emailService.sendEmail(email, "One Time Token", "link do zalogowania: " + link);
        this.redirectHandler.handle(request, response, oneTimeToken);
    }


}
