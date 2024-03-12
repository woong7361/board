package com.example.notice.exception;

public class BoardNotExistException extends EntityNotExistException{
    public BoardNotExistException(String message) {
        super(message);
    }

}
