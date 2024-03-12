package com.example.notice.exception;

public class AuthorizationException extends RuntimeException{
    public AuthorizationException(String message) {
        super(message);
    }
}
