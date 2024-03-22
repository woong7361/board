package com.example.notice.exception;

/**
 * 잘못된 파라미터 에러 클래스
 */
public class BadRequestParamException extends RuntimeException{
    public BadRequestParamException(String message) {
        super(message);
    }
}
