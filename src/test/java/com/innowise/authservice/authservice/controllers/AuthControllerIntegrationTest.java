package com.innowise.authservice.authservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.authservice.authservice.model.AuthUser;
import com.innowise.authservice.authservice.repository.AuthUserRepository;
import com.innowise.authservice.authservice.security.AuthUserDetails;
import com.innowise.authservice.authservice.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    private AuthUser testUser;
    private String accessToken;

    @BeforeEach
    void setUp() {
        authUserRepository.deleteAll();

        String rawPassword = "1234";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        testUser = new AuthUser();
        testUser.setUserName("maxim");
        testUser.setEmail("fsafsf@gmail.com");
        testUser.setPassword(encodedPassword);

        authUserRepository.save(testUser);


        accessToken = jwtService.generateToken(
                Map.of("userName", testUser.getUserName()),
                new AuthUserDetails(testUser)
        );
    }

    @Test
    void register_createsUser() throws Exception {
        AuthUser newUser = new AuthUser();
        newUser.setUserName("alex");
        newUser.setEmail("alex@gmail.com");
        newUser.setPassword("1234");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User has been registered"));
    }

    @Test
    void login_returnsTokens() throws Exception {
        Map<String, String> loginRequest = Map.of("userName", "maxim", "password", "1234");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void refresh_returnsNewAccessToken() throws Exception {
        String refreshToken = jwtService.generateToken(
                Map.of("userName", testUser.getUserName()),
                new AuthUserDetails(testUser)
        );

        Map<String, String> request = Map.of("refreshToken", refreshToken);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    void validate_returnsTrueForValidToken() throws Exception {
        Map<String, String> request = Map.of("token", accessToken);

        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }
}

