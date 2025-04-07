package org.green_building.excellent_training.security;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.green_building.excellent_training.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    private final SecretKey key;

    public JwtTokenProvider(@Value("${jwt.secret-key.base64-url}") String secret) {
        // Decode it to bytes (Base64URL to raw bytes)
        byte[] decodedKey = Base64.getUrlDecoder().decode(secret);
        // Create the SecretKey using HMAC-SHA512
        key = new SecretKeySpec(decodedKey, 0, decodedKey.length, SignatureAlgorithm.HS512.getJcaName());
    }

    /*
    public JwtTokenProvider(@Value("${jwt.secret-key.utf-8}") String secret) {
        // Encode the string to Base64 (URL safe)
        String base64EncodedSecret = Base64.getUrlEncoder().encodeToString(secret.getBytes());
        // Decode it back to bytes (Base64URL to raw bytes)
        byte[] decodedKey = Base64.getUrlDecoder().decode(base64EncodedSecret);
        // Create the SecretKey using HMAC-SHA512
        key = new SecretKeySpec(decodedKey, 0, decodedKey.length, SignatureAlgorithm.HS512.getJcaName());
    }
    */

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("role", user.getRole().getName());

        Date now = new Date();
        //  Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            // .setExpiration(validity)
            .signWith(key)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userId = claims.getSubject();
        String role = claims.get("role", String.class);

        // Create a collection with a single authority based on role
        Collection<SimpleGrantedAuthority> authorities = 
                List.of(new SimpleGrantedAuthority("ROLE_" + role));

        return new UsernamePasswordAuthenticationToken(userId, "", authorities);
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
