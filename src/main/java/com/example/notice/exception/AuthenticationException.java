package com.example.notice.exception;

/**
 * 인증 에러
 */
public class AuthenticationException extends RuntimeException{
    public AuthenticationException(String message) {
        super(message);
    }
}
