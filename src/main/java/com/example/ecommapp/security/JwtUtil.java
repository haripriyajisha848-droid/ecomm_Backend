package com.example.ecommapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    // reads jwt.secret from application.properties
    @Value("${jwt.secret}")
    private String secret;

    // reads jwt.expiration from application.properties
    @Value("${jwt.expiration}")
    private long expiration;

    // converts secret string into a cryptographic key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // called after login — generates a token for the user
    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .subject(email)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    // extracts email from token
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // extracts roles from token
    public List<String> extractRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    // checks if token is expired
    public boolean isTokenValid(String token) {
        try {
            return !extractAllClaims(token)
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // decodes and verifies the token signature
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}