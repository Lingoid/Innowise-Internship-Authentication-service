package com.innowise.authservice.authservice.util;

public class FailedRegistrationException extends RuntimeException{
    private static final String MESSAGE = "Registration failed, rolling back";

    public FailedRegistrationException() {
        super(MESSAGE);
    }
}
