package com.bookbase.backend.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.bookbase.backend.entity.Member;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JWTService {

    @Value("${jwt.secret.key}")
    String secretKey;

    public String generateToken(Member member) {
        log.info("Generating JWT token for userId={} username={} role={}",
                member.getMemberID(), member.getUserName(), member.getRole());

        String token = Jwts.builder()
                .subject(member.getUserName())
                .claim("userID", member.getMemberID())
                .claim("userName", member.getUserName())
                .claim("userRole", member.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 2 * 1000))
                .signWith(getKey())
                .compact();

        log.debug("JWT token generated successfully for username={}", member.getUserName());
        return token;
    }

    private SecretKey getKey() {
        log.trace("Decoding secret key for JWT signing");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        String username = extractClaim(token, Claims::getSubject);
        log.debug("Extracted username={} from JWT token", username);
        return username;
    }

    <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        log.trace("Extracting all claims from JWT token");
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String userName = extractUsername(token);
        boolean valid = (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));

        if (valid) {
            log.info("JWT token validated successfully for username={}", userName);
        } else {
            log.warn("JWT token validation failed for username={}", userName);
        }
        return valid;
    }

    private boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        if (expired) {
            log.warn("JWT token has expired");
        }
        return expired;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}