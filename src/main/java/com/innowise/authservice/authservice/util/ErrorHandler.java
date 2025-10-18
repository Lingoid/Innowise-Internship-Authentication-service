package com.innowise.authservice.authservice.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(AuthUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(AuthUserNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse("Internal server error", LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), LocalDateTime.now()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidTokenException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), LocalDateTime.now()),
                HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(FailedRegistrationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(FailedRegistrationException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), LocalDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), LocalDateTime.now()),
                HttpStatus.CONFLICT
        );
    }
}
