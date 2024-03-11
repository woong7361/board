package com.example.notice.exception;

public class BadRequestParamException extends RuntimeException{
    public BadRequestParamException(String message) {
        super(message);
    }
}
