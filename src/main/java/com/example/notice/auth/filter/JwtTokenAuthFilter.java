package com.example.notice.auth.filter;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.entity.Member;
import com.example.notice.auth.provider.AuthProvider;
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

    /**
     * 인증 필터 - JWT token을 통해 인증객체를 받아온다.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String bearerToken = request.getHeader(AUTHORIZATION);

        // Bearer token 이 null 이면 GUEST 처리
        // Bearer token 이 있다면 Member 처리?

        Member member = authProvider.verify(bearerToken);

        AuthenticationHolder.clear();
        try {
            AuthenticationHolder.setPrincipal(new MemberPrincipal(member));
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            AuthenticationHolder.clear();
        }
    }

}
