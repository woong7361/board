package com.example.notice.auth.filter;


import com.example.notice.auth.path.PathMethod;
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
 * admin 인증 interceptor
 */
@Component
public class AdminSessionInterceptor implements HandlerInterceptor {

    /**
     *  session을 통해 admin 인증을 진행한다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(PathMethod.OPTIONS.name())) {
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
