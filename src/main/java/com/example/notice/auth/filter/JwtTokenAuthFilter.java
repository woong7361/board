package com.example.notice.auth.filter;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.entity.Member;
import com.example.notice.auth.AuthProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * Authentication 검증하는 filter
 */
@RequiredArgsConstructor
public class JwtTokenAuthFilter implements Filter {

    private final AuthProvider authProvider;

    public static final String AUTHORIZATION = "Authorization";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String bearerToken = request.getHeader(AUTHORIZATION);

        // TODO filter에서 error 발생시 globalHandler 불가능, but. java Servlet 기술이라는 범용성
        // 인터셉터와 필터 어떤것을 사용할 것인가 생각해보기
        Member member = authProvider.verify(bearerToken);

        AuthenticationHolder.clear();
        try {
            AuthenticationHolder.setMember(member);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            AuthenticationHolder.clear();
        }
    }

}
