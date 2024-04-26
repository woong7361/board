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



@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    //TODO 굳이 생성자로 의존성을 드러낼 필요가 있는가? => 없어보인다.
    private static final PathContainer pathContainer = new PathContainer();

    public static final String OPTIONS_METHOD = "OPTIONS";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //TODO 원래 이러는 메서드인가?
        if (request.getMethod().equals(OPTIONS_METHOD)) {
            return true;
        }

        if (pathContainer.match(request.getRequestURI(), PathMethod.valueOf(request.getMethod()), AuthenticationHolder.getRole())) {
            return true;
        }
        throw new AuthorizationException(ErrorMessageConstant.AUTHORIZATION_EXCEPTION_MESSAGE);
    }

    public void includePathPatterns(String pathPattern, PathMethod pathMethod, AuthorizationRole role) {
        pathContainer.includePathPattern(pathPattern, pathMethod, role);
    }

    public void excludePathPatterns(String pathPattern, PathMethod pathMethod, AuthorizationRole role) {
        pathContainer.excludePathPattern(pathPattern, pathMethod, role);
    }
}
