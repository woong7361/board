package com.example.notice.auth.filter;


import com.example.notice.constant.SessionConstant;
import com.example.notice.entity.Member;
import com.example.notice.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;

@Component
public class AdminSessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new AuthenticationException("세션이 없습니다.");
        }

        if (session.getAttribute(ADMIN_SESSION_KEY) == null) {
            throw new AuthenticationException("잘못된 세션입니다.");
        }

        return true;

    }
}
