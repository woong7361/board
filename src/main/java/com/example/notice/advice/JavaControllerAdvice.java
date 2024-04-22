package com.example.notice.advice;

import com.example.notice.exception.AuthenticationException;
import com.example.notice.exception.BadRequestParamException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    /**
     * 잘못된 requestParameter 에러 엔드포인트
     * @param exception parameter 에러
     * @return 400 bad request
     */
    @ExceptionHandler(BadRequestParamException.class)
    public ResponseEntity<ErrorResponse> badRequestParameterException(BadRequestParamException exception) {
        log.debug("illegalArgument exception - message: {}", exception.getMessage());
        log.info("trace: {}", exception);

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .message(exception.getMessage())
                        .build());

    }

    /**
     * 인증 에러 엔드포인트
     * @param exception 인증 에러
     * @return 401 unAuthorized
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationException(AuthenticationException exception) {
        log.debug("unAuthentication exception - message: {}", exception.getMessage());
        log.info("trace: {}", exception);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .message(exception.getMessage())
                        .build());

    }


//    ExpiredJwtException
//    UnsupportedJwtException
//    MalformedJwtException
//    SignatureException
//    IllegalArgumentException
    /**
     * jwt 만료
     * @param exception jwt 만료 exception
     * @return 401 unAuthorized
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> jwtExpiredException(ExpiredJwtException exception) {
        log.debug("jwt expired exception - message: {}", exception.getMessage());
        log.info("trace: {}", exception);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .message("JWT EXPIRED")
                        .build());

    }

    /**
     * jwt 관련 에러
     * JwtException 하위 23개 포함
     * @param exception jwt 에러
     * @return 401 unAuthorized
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> jwtException(JwtException exception) {
        log.debug("jwt exception - message: {}", exception.getMessage());
        log.info("trace: {}", exception);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .message("JWT EXCEPTION")
                        .build());

    }

}
