package com.innowise.authservice.authservice.service;

import com.innowise.authservice.authservice.model.AuthUser;
import com.innowise.authservice.authservice.repository.AuthUserRepository;
import com.innowise.authservice.authservice.util.AuthUserNotFoundException;
import com.innowise.authservice.authservice.util.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private LoginService loginService;

    private AuthUser user;

    @BeforeEach
    void setUp() {
        user = new AuthUser();
        user.setId(1L);
        user.setUserName("maxim");
        user.setPassword("$2a$10$encodedPassword");
    }

    @Test
    void login_returnsTokens_whenCredentialsValid() {
        when(authUserRepository.findByUserName("maxim")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234", user.getPassword())).thenReturn(true);
        when(tokenService.generateAccessToken(user)).thenReturn("accessToken");
        when(tokenService.generateRefreshToken(user)).thenReturn("refreshToken");

        Map<String, String> result = loginService.login("maxim", "1234");

        assertAll(() -> {
            assertEquals("accessToken", result.get("accessToken"));
            assertEquals("refreshToken", result.get("refreshToken"));
        });
    }

    @Test
    void login_throwsException_whenUserNotFound() {
        when(authUserRepository.findByUserName("maxim")).thenReturn(Optional.empty());

        assertThrows(AuthUserNotFoundException.class, () -> loginService.login("maxim", "1234"));
    }

    @Test
    void login_throwsException_whenPasswordInvalid() {
        when(authUserRepository.findByUserName("maxim")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> loginService.login("maxim", "wrong"));
    }
}