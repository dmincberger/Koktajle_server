package springboot_ds.koktajle.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Jwts;
import springboot_ds.koktajle.model.User;

@Service
public class JwtService {
    private static final Key SIGNING_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(String email, long expiration_time) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration_time);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token, User user) {
        Date expirationDate = getExpirationDateFromToken(token);
        assert expirationDate != null;
        if (expirationDate.before(new Date())) {
            System.out.println("INVALID BO EXPIRATION JEST Z PRZESZLOSCI");
            return false;
        }
        String email = getEmailFromToken(token);
        return user.getEmail().equals(email) && !expirationDate.before(new Date());
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("email", String.class);
        } catch (ExpiredJwtException e) {
            // Token expired
            return "";
        }
    }

    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("exp", Date.class);
        } catch (ExpiredJwtException e) {
            // Token expired
            return null;
        }
    }
}
