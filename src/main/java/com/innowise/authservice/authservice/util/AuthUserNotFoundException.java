package com.innowise.authservice.authservice.util;

public class AuthUserNotFoundException extends RuntimeException {
    private static final String MESSAGE = "User not found";

    public AuthUserNotFoundException() {
        super(MESSAGE);
    }
}
