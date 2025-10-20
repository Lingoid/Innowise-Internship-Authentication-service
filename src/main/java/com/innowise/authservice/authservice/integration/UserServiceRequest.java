package com.innowise.authservice.authservice.integration;

import com.innowise.authservice.authservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserServiceRequest {

    private final WebClient webClient;

    public UserServiceRequest(@Value("${user.service.url}") String userServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

    public void createUserProfile(UserDTO userDTO) {
        webClient.post()
                .uri("")
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
