package com.example.notice.auth.filter;

import com.example.notice.auth.AuthProvider;
import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 사용자 인증 인터셉터
 */
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    public static final String OPTIONS_METHOD = "OPTIONS";
    private final AuthProvider authProvider;
    public static final String AUTHORIZATION = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //TODO 따로 해결할 방법
        if (request.getMethod().equals(OPTIONS_METHOD)) {
            return true;
        }

        String bearerToken = request.getHeader(AUTHORIZATION);

        Member member = authProvider.verify(bearerToken);

        AuthenticationHolder.clear();
        AuthenticationHolder.setPrincipal(new MemberPrincipal(member));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthenticationHolder.clear();
    }
}
