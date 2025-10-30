package com.switchapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    // 256-bit key (use environment variable in production)
    private static final String SECRET_KEY = "your256bitsecretkeyyour256bitsecretkey";

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(
                        java.util.Base64.getEncoder().encodeToString(SECRET_KEY.getBytes())
                )
        );
    }

    public String generateToken(String username, Map<String, Object> claims) {
        long expiryMs = 1000 * 60 * 60; // 1 hour
        return Jwts.builder()
                .setClaims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiryMs))
                .signWith(getSignKey())
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
