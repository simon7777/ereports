package sk.dekret.ereports.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import sk.dekret.ereports.db.entities.UserAccount;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {

    private static final long EXPIRE_DURATION = (long) 24 * 60 * 60 * 1000; // 24 hours
    private static final String ROLE_CLAIM_KEY = "role";
    private static final String USER_ACCOUNT_ID_KEY = "userAccountId";

    @Value("${app.jwt.secret}")
    private String secret;

    public String generateAccessToken(UserAccount user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuer("eReports")
                .claim(ROLE_CLAIM_KEY, user.getRole())
                .claim(USER_ACCOUNT_ID_KEY, user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired. %s", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace. %s", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            log.error("Signature validation failed");
        }

        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Long getUserAccountId(String token) {
        Object userAccountId = parseClaims(token).get(USER_ACCOUNT_ID_KEY);

        if (userAccountId instanceof Number value) {
            return value.longValue();
        }

        return null;
    }

    public Collection<GrantedAuthority> getAuthorities(String token) {
        Object role = parseClaims(token).get(ROLE_CLAIM_KEY);

        if (role instanceof String value) {
            return Arrays.asList(new SimpleGrantedAuthority(value));
        }

        return Collections.emptyList();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
