package com.example.notice.exception;

/**
 * 파일 저장 오류
 */
public class FileSaveException extends RuntimeException{
    public FileSaveException(String message) {
        super(message);
    }
}
