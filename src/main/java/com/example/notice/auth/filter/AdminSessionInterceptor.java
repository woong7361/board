package com.example.notice.auth.filter;


import com.example.notice.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.example.notice.constant.ErrorMessageConstant.INVALID_SESSION_MESSAGE;
import static com.example.notice.constant.ErrorMessageConstant.NOT_SESSION_MESSAGE;
import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;

/**
 * admin이 인증 되어있는지 확인하는 admin session interceptor
 * interceptor는 MvcTest에서 MVC에 포함되어 있나봄 - 자동 bean injection 진행됨
 */
@Component
public class AdminSessionInterceptor implements HandlerInterceptor {
    public static final String OPTIONS_METHOD = "OPTIONS";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(OPTIONS_METHOD)) {
            return true;
        }

        checkAdminAuthentication(request.getSession(false));

        return true;
    }

    private void checkAdminAuthentication(HttpSession session) {
        if (session == null) {
            throw new AuthenticationException(NOT_SESSION_MESSAGE);
        }

        if (session.getAttribute(ADMIN_SESSION_KEY) == null) {
            throw new AuthenticationException(INVALID_SESSION_MESSAGE);
        }
    }
}
