package com.example.notice.exception;

public class PhysicalFileNotFoundException extends RuntimeException {
    public PhysicalFileNotFoundException(String message) {
        super(message);
    }
}
