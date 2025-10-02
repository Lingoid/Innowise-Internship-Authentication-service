package com.innowise.authservice.authservice.util;

public class InvalidTokenException extends RuntimeException {
    private static final String MESSAGE = "Invalid or expired token";

    public InvalidTokenException() {
        super(MESSAGE);
    }
}
