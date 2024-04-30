package com.example.notice.auth.path;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 요청 경로 저장을 위한 DTO
 */
@Getter
@AllArgsConstructor
public class PathWithRole {
    private String path;
    private PathMethod method;
    private AuthorizationRole role;
}
