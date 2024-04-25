package com.example.notice.auth.filter;

import com.example.notice.auth.provider.AuthProvider;
import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.auth.path.AuthorizationRole;
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

    private final AuthProvider authProvider;

    public static final long INVALID_MEMBER_ID = -1L;
    public static final String OPTIONS_METHOD = "OPTIONS";
    public static final String AUTHORIZATION = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(OPTIONS_METHOD)) {
            return true;
        }

        String bearerToken = request.getHeader(AUTHORIZATION);

        AuthenticationHolder.clear();
        if (bearerToken == null) {
            AuthenticationHolder.setPrincipal(new MemberPrincipal(
                    Member.builder()
                            .memberId(INVALID_MEMBER_ID)
                            .build(),
                    AuthorizationRole.GUEST));
        } else {
            Member member = authProvider.verify(bearerToken);
            AuthenticationHolder.setPrincipal(new MemberPrincipal(member, AuthorizationRole.MEMBER));
        }

        return true;

//        try {
//            Member member = authProvider.verify(bearerToken);
//            AuthenticationHolder.clear();
//            AuthenticationHolder.setPrincipal(new MemberPrincipal(member));
//        } catch (Exception e) {
//            if (request.getMethod().equals("GET")) {
//                return true;
//            } else{
//                throw e;
//            }
//        }
//        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthenticationHolder.clear();
    }
}
