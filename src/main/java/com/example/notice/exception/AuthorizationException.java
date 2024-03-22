package com.example.notice.exception;

/**
 * 인가 에러 클래스
 */
public class AuthorizationException extends RuntimeException{
    public AuthorizationException(String message) {
        super(message);
    }
}
