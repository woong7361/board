package com.example.notice.auth.filter;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.path.AuthorizationRole;
import com.example.notice.auth.path.PathContainer;
import com.example.notice.auth.path.PathMethod;
import com.example.notice.constant.ErrorMessageConstant;
import com.example.notice.exception.AuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 사용자 인가 interceptor
 */
@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final PathContainer pathContainer = new PathContainer();
    /**
     * AuthenticationRole에 따라 인가 과정을 진행한다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(PathMethod.OPTIONS.name())) {
            return true;
        }

        if (pathContainer.match(request.getRequestURI(), PathMethod.valueOf(request.getMethod()), AuthenticationHolder.getRole())) {
            return true;
        }
        throw new AuthorizationException(ErrorMessageConstant.AUTHORIZATION_EXCEPTION_MESSAGE);
    }

    /**
     * 허용되는 요청 경로를 추가한다.
     *
     * @param pathPattern 요청 경로
     * @param pathMethod 요청 메서드
     * @param role 필요한 권한
     */
    public void includePathPatterns(String pathPattern, PathMethod pathMethod, AuthorizationRole role) {
        pathContainer.includePathPattern(pathPattern, pathMethod, role);
    }

    /**
     * 제외할 요청 경로를 추가한다.
     *
     * @param pathPattern 요청 경로
     * @param pathMethod 요청 메서드
     * @param role 필요한 권한
     */
    public void excludePathPatterns(String pathPattern, PathMethod pathMethod, AuthorizationRole role) {
        pathContainer.excludePathPattern(pathPattern, pathMethod, role);
    }
}
