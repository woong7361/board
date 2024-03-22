package com.example.notice.exception;

/**
 * 게시글이 존재하지 않는다는 에러 클래스
 */
public class BoardNotExistException extends EntityNotExistException{
    public BoardNotExistException(String message) {
        super(message);
    }

}
