package com.example.notice.auth.filter;


import com.example.notice.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;

/**
 * admin이 인증 되어있는지 확인하는 admin session interceptor
 * interceptor는 MvcTest에서 MVC에 포함되어 있나봄 - 자동 bean injection 진행됨
 */
@Component
public class AdminSessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        checkAdminAuthentication(request.getSession(false));

        return true;
    }

    private void checkAdminAuthentication(HttpSession session) {
        if (session == null) {
            throw new AuthenticationException("세션이 없습니다.");
        }

        if (session.getAttribute(ADMIN_SESSION_KEY) == null) {
            throw new AuthenticationException("잘못된 세션입니다.");
        }
    }
}
