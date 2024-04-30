package com.example.notice.auth.path;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 요청 경로를 관리하는 경로 저장소
 */
public class PathContainer {
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final List<PathWithRole> includePathPattern = new ArrayList<>();
    private final List<PathWithRole> excludePathPattern = new ArrayList<>();

    /**
     * 경로 저장소에 경로를 추가한다
     * @param pathPattern 요청 경로
     * @param pathMethod 요청 메서드
     * @param role 권한
     */
    public void includePathPattern(String pathPattern, PathMethod pathMethod, AuthorizationRole role) {
        this.includePathPattern.add(new PathWithRole(pathPattern, pathMethod, role));
    }

    /**
     * 경로 저장소에 경로를 제외한다.
     * @param pathPattern 요청 경로
     * @param pathMethod 요청 메서드
     * @param role 권한
     */
    public void excludePathPattern(String pathPattern, PathMethod pathMethod, AuthorizationRole role) {
        this.excludePathPattern.add(new PathWithRole(pathPattern, pathMethod, role));
    }

    /**
     * 요청 경로가 저장된 경로에 해당하는지 판별한다.
     * @param targetPath 요청 경로
     * @param pathMethod 요청 메서드
     * @param role 권한
     * @return 판별 결과
     */
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
