package com.example.notice.advice;

import lombok.Builder;
import lombok.Getter;

/**
 * 공용 exception response
 */
@Builder
@Getter
public class ErrorResponse {
    private String message;
}


