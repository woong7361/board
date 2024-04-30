package com.example.notice.advice;

import lombok.Builder;
import lombok.Getter;

/**
 * 공용 exception response 클래스
 */
@Builder
@Getter
public class ErrorResponse {
    private String message;
}


