package com.example.notice.exception;

/**
 * entity를 찾을 수 없는 에러 클래스
 */
public class EntityNotExistException extends RuntimeException{
    public EntityNotExistException(String message) {
        super(message);
    }
}
