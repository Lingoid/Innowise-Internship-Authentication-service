package com.innowise.authservice.authservice.service;

import com.innowise.authservice.authservice.model.AuthUser;
import com.innowise.authservice.authservice.repository.AuthUserRepository;
import com.innowise.authservice.authservice.util.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(AuthUser user) {
        if (authUserRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        authUserRepository.save(user);
    }
}