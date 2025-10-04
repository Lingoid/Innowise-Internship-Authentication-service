package com.innowise.authservice.authservice.service;

import com.innowise.authservice.authservice.model.AuthUser;
import com.innowise.authservice.authservice.repository.AuthUserRepository;
import com.innowise.authservice.authservice.util.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationService registrationService;

    private AuthUser user;

    @BeforeEach
    void setUp() {
        user = new AuthUser();
        user.setUserName("maxim");
        user.setPassword("1234");
    }

    @Test
    void register_savesUser_whenNotExists() {
        when(authUserRepository.findByUserName("maxim")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("encodedPassword");

        registrationService.register(user);

        verify(authUserRepository).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void register_throwsException_whenUserExists() {
        when(authUserRepository.findByUserName("maxim")).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> registrationService.register(user));

        verify(authUserRepository, never()).save(any());
    }
}