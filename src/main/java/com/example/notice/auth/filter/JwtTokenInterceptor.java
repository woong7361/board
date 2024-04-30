package com.example.notice.auth.filter;

import com.example.notice.auth.path.PathMethod;
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
 * JWT 사용자 인증 interceptor
 */
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final AuthProvider authProvider;

    public static final long INVALID_MEMBER_ID = -1L;
    public static final String AUTHORIZATION = "Authorization";

    /**
     * JWT를 통해 인증 과정을 진행한다.
     * @apiNote token이 없다면 비회원으로, 있다면 회원으로 다음 interceptor로 진행한다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(PathMethod.OPTIONS.name())) {
            return true;
        }

        String bearerToken = request.getHeader(AUTHORIZATION);

        AuthenticationHolder.clear();
        if (bearerToken == null) {
            setGuest();
        } else {
            setMember(bearerToken);
        }

        return true;
    }

    /**
     * 인증 저장소를 비워준다.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthenticationHolder.clear();
    }

    private void setMember(String bearerToken) {
        Member member = authProvider.verify(bearerToken);
        AuthenticationHolder.setPrincipal(new MemberPrincipal(member, AuthorizationRole.MEMBER));
    }

    private static void setGuest() {
        AuthenticationHolder.setPrincipal(new MemberPrincipal(
                Member.builder()
                        .memberId(INVALID_MEMBER_ID)
                        .build(),
                AuthorizationRole.GUEST));
    }
}
