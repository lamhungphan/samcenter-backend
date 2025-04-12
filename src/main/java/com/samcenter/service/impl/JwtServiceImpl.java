package com.samcenter.service.impl;

import com.samcenter.common.TokenType;
import com.samcenter.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.expiryMinutes}")
    private long expMinutes;

    @Value("${jwt.expiryDay}")
    private long expDay;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Override
    public String generateAccessToken(String username, List<String> authorities) {
        log.info("Generating Access Token for user: {} with roles: {}", username, authorities);
        return generateToken(username, authorities, TokenType.ACCESS_TOKEN);    }

    @Override
    public String generateRefreshToken(String username, List<String> authorities) {
        log.info("Generating Refresh Token for user: {} with roles: {}", username, authorities);
        return generateToken(username, authorities, TokenType.REFRESH_TOKEN);    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    private String generateToken(String username, List<String> authorities, TokenType type) {
        Map<String, Object> claims = Map.of("role", authorities); // them id vao token
        Date now = new Date();
        Date expiration = new Date(now.getTime() + getExpirationTime(type));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getKey(type), SignatureAlgorithm.HS512)
                .compact();
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimsResolver) {
        Claims claims = parseToken(token, type);
        return claimsResolver.apply(claims);
    }

    private Claims parseToken(String token, TokenType type) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey(type))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AccessDeniedException("Token expired: " + e.getMessage());
        } catch (SignatureException | MalformedJwtException e) {
            throw new AccessDeniedException("Invalid token: " + e.getMessage());
        }
    }

    private Key getKey(TokenType type) {
        String secret = (type == TokenType.ACCESS_TOKEN) ? accessKey : refreshKey;
//        byte[] keyBytes = Base64.getUrlDecoder().decode(secret); // URL-SAFE DECODER
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private long getExpirationTime(TokenType type) {
        return (type == TokenType.ACCESS_TOKEN) ? expMinutes * 60 * 1000 : expDay * 24 * 60 * 60 * 1000;
    }
}
