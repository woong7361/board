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
 * 에러를 캐치한다.
 */
@RestControllerAdvice
@Slf4j
public class SpringControllerAdvice {

    /**
     * '@Valid'에 관한 에러를 잡는다.
     *
     * @param exception MethodArgumentNotValidException
     * @return 에러 페이지로 이동한다.
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

    /**
     * 컨트롤러의 필수 파라미터가 결여되어있을때 발생하는 exception endpoint (ex. pathVariable)
     * @param exception 컨트롤러 필수 파라미터 결여 exception
     * @return 400 bad request
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValid(MissingServletRequestParameterException exception) {
        log.debug("field error - name: {}, type: {}, message: {}",
                exception.getParameterName(),
                exception.getParameterType(),
                exception.getMessage());

        log.info("{}", exception);

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .message(exception.getMessage())
                        .build()
                );
    }
}
