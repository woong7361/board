package com.example.notice.auth.path;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PathWithRole {
    private String path;
    private PathMethod method;
    private AuthorizationRole role;
}
