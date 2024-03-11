package com.example.notice.exception;

/**
 * entity를 찾을 수 없음
 */
public class EntityNotExistException extends RuntimeException{
    public EntityNotExistException(String message) {
        super(message);
    }
}
