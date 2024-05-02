package com.example.notice.exception;

/**
 * 물리적 파일 저장 에러 클래스
 */
public class FileSaveCheckedException extends Exception{

    public FileSaveCheckedException(String message) {
        super(message);
    }

}
