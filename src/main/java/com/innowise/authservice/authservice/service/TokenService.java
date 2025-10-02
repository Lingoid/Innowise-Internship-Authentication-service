package com.innowise.authservice.authservice.service;

import com.innowise.authservice.authservice.model.AuthUser;
import com.innowise.authservice.authservice.security.AuthUserDetails;
import com.innowise.authservice.authservice.util.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;

    public String generateAccessToken(AuthUser user) {
        return jwtService.generateToken(Map.of(), new AuthUserDetails(user));
    }

    public String generateRefreshToken(AuthUser user) {
        return jwtService.generateRefreshToken(new AuthUserDetails(user));
    }

    public boolean validateToken(String token, AuthUser user) {
        boolean valid = jwtService.isTokenValid(token, new AuthUserDetails(user));
        if (!valid) {
            throw new InvalidTokenException();
        }
        return valid;

    }

    public String extractUsername(String token) {
        return jwtService.extractUserName(token);
    }
}
