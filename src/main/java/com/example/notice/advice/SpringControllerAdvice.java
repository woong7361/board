package com.example.notice.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * spring exception의 controller advice
 */
@RestControllerAdvice
@Slf4j
public class SpringControllerAdvice {

    /**
     * '@Valid', @Validated 관한 에러를 잡는다.
     *
     * @param exception MethodArgumentNotValidException
     * @return 400 bad request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValid(MethodArgumentNotValidException exception) {
        for (FieldError fieldError : exception.getFieldErrors()) {
            log.debug("field error - field: {}, message: {}",  fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.info("{}", exception);

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .message(exception.getMessage())
                        .build()
                );
    }
}
