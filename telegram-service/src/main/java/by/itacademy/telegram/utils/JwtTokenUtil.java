package by.itacademy.telegram.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret")
    private String jwtSecret;
    @Value("${jwt.issuer}")
    private String jwtIssuer;

    public boolean isExpired(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) {
            return false;
        }
        return claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String input) {
        String token = null;
        if (input.startsWith("Bearer ")) {
            token = input.split(" ")[1].trim();
        }
        if (token == null) {
            return null;
        }
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}
