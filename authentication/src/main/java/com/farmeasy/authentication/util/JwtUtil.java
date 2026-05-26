package com.farmeasy.authentication.util;

import com.farmeasy.authentication.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final SecretKey key;
    private final long jwtExpirationMs;
    private final long refreshExpirationMs;

    public JwtUtil(
            @Value("${farmeasy.jwt.secret}") String jwtSecret,
            @Value("${farmeasy.jwt.access-expiration-ms:3600000}") long jwtExpirationMs,
            @Value("${farmeasy.jwt.refresh-expiration-days:7}") int refreshDays) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
        this.refreshExpirationMs = refreshDays * 24L * 60 * 60 * 1000;
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getUserId().toString())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles() == null ? "" : user.getRoles().stream()
                        .map(r -> r.getRoleName()).collect(Collectors.joining(",")))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getUserId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(key)
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        String subject = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload().getSubject();
        return UUID.fromString(subject);
    }
}
