package com.example.notice.filter;

import com.example.notice.entity.Member;
import com.example.notice.service.AuthProvider;
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

        Member member = authProvider.verify(bearerToken);

        try {
            AuthenticationHolder.setMember(member);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            //TODO remove 도중 에러가 발생한다면? -> 여러번 삭제시도?
            AuthenticationHolder.clear();
        }
    }

}
