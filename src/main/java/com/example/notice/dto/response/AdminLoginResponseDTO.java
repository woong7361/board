package com.example.notice.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 관리자 로그인 응답 DTO
 */
@Getter
@Builder
public class AdminLoginResponseDTO {
    private Long memberId;
    private String name;
    private Integer sessionTimeOut;
}
