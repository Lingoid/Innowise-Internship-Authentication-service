package com.innowise.authservice.authservice.service;

import com.innowise.authservice.authservice.model.AuthUser;
import com.innowise.authservice.authservice.repository.AuthUserRepository;
import com.innowise.authservice.authservice.util.AuthUserNotFoundException;
import com.innowise.authservice.authservice.util.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public Map<String, String> login(String username, String password) {
        AuthUser user = authUserRepository.findByUserName(username)
                .orElseThrow(AuthUserNotFoundException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }
}
