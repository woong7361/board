package com.example.notice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminLoginResponse {
    private Long memberId;
    private String name;
    private Integer sessionTimeOut;
}
