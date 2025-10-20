package com.innowise.authservice.authservice.service;

import com.innowise.authservice.authservice.dto.FullUserInfoDTO;
import com.innowise.authservice.authservice.dto.UserDTO;
import com.innowise.authservice.authservice.integration.UserServiceRequest;
import com.innowise.authservice.authservice.model.AuthUser;
import com.innowise.authservice.authservice.repository.AuthUserRepository;
import com.innowise.authservice.authservice.util.FailedRegistrationException;
import com.innowise.authservice.authservice.util.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceRequest userServiceRequest;

    @Transactional
    public void register(FullUserInfoDTO fullUserInfoDTO) {
        if (authUserRepository.findByUserName(fullUserInfoDTO.getUserName()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        AuthUser authUser = new AuthUser();
        authUser.setUserName(fullUserInfoDTO.getUserName());
        authUser.setEmail(fullUserInfoDTO.getEmail());
        authUser.setPassword(passwordEncoder.encode(fullUserInfoDTO.getPassword()));
        authUserRepository.save(authUser);
        authUser.setUserId(authUser.getId());
        authUserRepository.updateUserId(authUser);


        UserDTO userDTO = new UserDTO();
        userDTO.setId(authUser.getId());
        userDTO.setName(fullUserInfoDTO.getName());
        userDTO.setSurname(fullUserInfoDTO.getSurname());
        userDTO.setBirthDate(fullUserInfoDTO.getBirthDate());
        userDTO.setEmail(fullUserInfoDTO.getEmail());

        try {
            userServiceRequest.createUserProfile(userDTO);
        } catch (Exception e) {
            throw new FailedRegistrationException();
        }
    }
}