package com.innowise.authservice.authservice.controller;

import com.innowise.authservice.authservice.model.AuthUser;
import com.innowise.authservice.authservice.repository.AuthUserRepository;
import com.innowise.authservice.authservice.service.LoginService;
import com.innowise.authservice.authservice.service.RegistrationService;
import com.innowise.authservice.authservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;
    private final LoginService loginService;
    private final TokenService tokenService;
    private final AuthUserRepository authUserRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthUser authUser){
        registrationService.register(authUser);
        return ResponseEntity.ok("User has been registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(loginService.login(request.get("userName"), request.get("password")));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String username = tokenService.extractUsername(refreshToken);

        AuthUser user = authUserRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!tokenService.validateToken(refreshToken, user)) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        return ResponseEntity.ok(Map.of("accessToken", tokenService.generateAccessToken(user)));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String username = tokenService.extractUsername(token);

        return authUserRepository.findByUserName(username)
                .filter(user -> tokenService.validateToken(token, user))
                .map(u -> ResponseEntity.ok(Map.of("valid", true)))
                .orElse(ResponseEntity.ok(Map.of("valid", false)));
    }
}
