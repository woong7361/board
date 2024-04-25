package com.example.notice.auth.path;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.List;

public class PathContainer {
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final List<PathWithRole> includePathPattern = new ArrayList<>();
    private final List<PathWithRole> excludePathPattern = new ArrayList<>();

    public void includePathPattern(String pathPattern, PathMethod pathMethod, AuthorizationRole role) {
        this.includePathPattern.add(new PathWithRole(pathPattern, pathMethod, role));
    }

    public void excludePathPattern(String pathPattern, PathMethod pathMethod, AuthorizationRole role) {
        this.excludePathPattern.add(new PathWithRole(pathPattern, pathMethod, role));
    }

    public Boolean match(String targetPath, PathMethod pathMethod, AuthorizationRole role) {
        boolean includeMatchResult = includePathPattern.stream()
                .anyMatch(pathWithRole -> anyMatchPattern(pathWithRole, targetPath, pathMethod, role));

        boolean excludeMatchResult = excludePathPattern.stream()
                .anyMatch(pathWithRole -> anyMatchPattern(pathWithRole, targetPath, pathMethod, role));

        return includeMatchResult && !excludeMatchResult;
    }

    private Boolean anyMatchPattern(PathWithRole pathWithRole, String targetPath, PathMethod pathMethod, AuthorizationRole role) {
        return pathMatcher.match(pathWithRole.getPath(), targetPath) &&
                PathMethod.match(pathWithRole.getMethod(), pathMethod) &&
                AuthorizationRole.match(pathWithRole.getRole(), role);
    }


}
