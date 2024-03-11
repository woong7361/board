package com.example.notice.exception;

/**
 * 물리적 파일 저장 exception
 */
public class FileSaveCheckedException extends Exception{
    private String fileName;

    public FileSaveCheckedException(String message, String fileName) {
        super(message);
        this.fileName = fileName;
    }

}
