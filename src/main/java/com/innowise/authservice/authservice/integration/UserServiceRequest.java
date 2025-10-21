package com.innowise.authservice.authservice.integration;

import com.innowise.authservice.authservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserServiceRequest {

    private final WebClient webClient;
    private final String secretWord;

    public UserServiceRequest(@Value("${user.service.url}") String userServiceUrl,
                              @Value("${registration.secret-word}") String secretWord) {
        this.webClient = WebClient.builder()
                .baseUrl(userServiceUrl)
                .build();
        this.secretWord = secretWord;
    }

    public void createUserProfile(UserDTO userDTO) {
        webClient.post()
                .uri("")
                .header("secret", secretWord)
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
