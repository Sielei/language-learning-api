package com.hs.languagelearningapi.auth;

import com.hs.languagelearningapi.common.ApplicationConfigData;
import com.hs.languagelearningapi.common.DTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Component
class JwtUtil {
    private final ApplicationConfigData configData;
    private SecretKey secretKey;

    public JwtUtil(ApplicationConfigData configData) {
        this.configData = configData;
        this.secretKey = Keys.hmacShaKeyFor(configData.getJwtSecret().getBytes());
    }

    public String generateJWTToken(DTO.UserDto user){
        return Jwts.builder()
                .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .issuer("Language API")
                .claim("userId", user.id())
                .claim("username", user.email())
                .expiration(Date.from(ZonedDateTime.now().plusMinutes(configData.getJwtExpiryDurationInMinutes()).toInstant()))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateJWTToken(String token){
        return getUsername(token) != null && isExpired(token);
    }

    private boolean isExpired(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.get("username").toString();
    }
    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
    public UUID getUserIdFromJWTToken(String token){
        return UUID.fromString(getClaims(token).get("userId").toString());
    }
}
