package com.bookbase.backend.service;

import com.bookbase.backend.entity.Member;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    @InjectMocks
    private JWTService jwtService;

    private Member sampleMember;
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService.secretKey = "2ZvU/Ri4Soo6NcF9wlJngq3RB8rGRtN25jVpOOi9x6k=";

        sampleMember = new Member();
        sampleMember.setMemberID(1);
        sampleMember.setUserName("testuser");
        sampleMember.setRole("USER");

        token = jwtService.generateToken(sampleMember);
    }

    @Test
    void testGenerateToken() {
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testExtractUsername() {
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken_Valid() {
        UserDetails userDetails = new User("testuser", "password", List.of());
        assertTrue(jwtService.validateToken(token, userDetails));
    }

    @Test
    void testValidateToken_InvalidUsername() {
        UserDetails userDetails = new User("wronguser", "password", List.of());
        assertFalse(jwtService.validateToken(token, userDetails));
    }

    @Test
    void testIsTokenExpired_False() {
        assertFalse(jwtService.validateToken(token, new User("testuser", "password", List.of())));
    }

    @Test
    void testExtractExpiration() {
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }
}
