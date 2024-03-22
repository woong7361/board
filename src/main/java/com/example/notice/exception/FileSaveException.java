package com.example.notice.exception;

/**
 * 파일 저장 에러 클래스
 */
public class FileSaveException extends RuntimeException{
    public FileSaveException(String message) {
        super(message);
    }
}
