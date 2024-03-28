package com.example.notice.exception;

/**
 * 물리적 파일을 찾지 못한다는 exception
 */
public class PhysicalFileNotFoundException extends RuntimeException {
    public PhysicalFileNotFoundException(String message) {
        super(message);
    }
}
