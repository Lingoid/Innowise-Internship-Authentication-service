package com.innowise.authservice.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FullUserInfoDTO {

    private Long id;
    private String userName;
    private String password;
    private String email;
    private String name;
    private String surname;
    private LocalDate birthDate;
}
