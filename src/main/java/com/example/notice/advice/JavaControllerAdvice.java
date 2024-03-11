package com.example.notice.advice;

import com.example.notice.exception.BadRequestParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * java exception의 controller advice
 * 에러를 캐치한다.
 */
@RestControllerAdvice
@Slf4j
public class JavaControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException exception) {
        log.debug("illegalArgument exception - message: {}", exception.getMessage());
        log.debug("illegalArgument exception - trace: {}", exception);

        log.info("{}", exception);

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .message(exception.getMessage())
                        .build());

    }

    @ExceptionHandler(BadRequestParamException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentException(BadRequestParamException exception) {
        log.debug("illegalArgument exception - message: {}", exception.getMessage());
        log.info("trace: {}", exception);

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .message(exception.getMessage())
                        .build());

    }

}
