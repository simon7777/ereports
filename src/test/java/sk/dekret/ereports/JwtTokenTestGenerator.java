package sk.dekret.ereports;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import sk.dekret.ereports.db.entities.UserRole;

import java.util.Date;

public interface JwtTokenTestGenerator {

    String getSecret();

    default String getJwtTokenForManager(String username) {
        return getJwtToken(username, UserRole.ROLE_MANAGER);
    }

    default String getJwtTokenForUser(String username) {
        return getJwtToken(username, UserRole.ROLE_USER);
    }

    default String getJwtToken(String username, UserRole role) {
        return "Bearer " + Jwts.builder()
                .setSubject(username)
                .setIssuer("eReports")
                .claim("role", role.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
                .signWith(SignatureAlgorithm.HS512, getSecret())
                .compact();
    }
}
