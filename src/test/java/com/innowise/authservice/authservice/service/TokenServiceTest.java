package com.innowise.authservice.authservice.service;

import com.innowise.authservice.authservice.model.AuthUser;
import com.innowise.authservice.authservice.util.InvalidTokenException;
import com.innowise.authservice.authservice.security.AuthUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TokenService tokenService;

    private AuthUser user;

    @BeforeEach
    void setUp() {
        user = new AuthUser();
        user.setId(1L);
        user.setUserName("maxim");
    }

    @Test
    void generateAccessToken_returnsToken() {
        when(jwtService.generateToken(anyMap(), any(AuthUserDetails.class)))
                .thenReturn("accessToken");

        String token = tokenService.generateAccessToken(user);

        assertAll(() -> {
            assertNotNull(token);
            assertEquals("accessToken", token);
        });
    }

    @Test
    void generateRefreshToken_returnsToken() {
        when(jwtService.generateRefreshToken(any(AuthUserDetails.class)))
                .thenReturn("refreshToken");

        String token = tokenService.generateRefreshToken(user);
        assertAll(() -> {
            assertNotNull(token);
            assertEquals("refreshToken", token);
        });
    }

    @Test
    void validateToken_returnsTrue_whenTokenValid() {
        when(jwtService.isTokenValid(any(String.class), any(AuthUserDetails.class)))
                .thenReturn(true);

        boolean result = tokenService.validateToken("someToken", user);

        assertTrue(result);
    }

    @Test
    void validateToken_throwsException_whenTokenInvalid() {
        when(jwtService.isTokenValid(any(String.class), any(AuthUserDetails.class)))
                .thenReturn(false);

        assertThrows(InvalidTokenException.class,
                () -> tokenService.validateToken("invalidToken", user));
    }

    @Test
    void extractUsername_returnsUsername() {
        when(jwtService.extractUserName("token")).thenReturn("maxim");

        String username = tokenService.extractUsername("token");

        assertEquals("maxim", username);
    }
}
