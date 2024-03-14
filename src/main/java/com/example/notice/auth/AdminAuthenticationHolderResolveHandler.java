package com.example.notice.auth;

import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.entity.Member;
import com.example.notice.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;

/**
 * 관리자 인증 객체 ResolveHandler
 */
@Component
public class AdminAuthenticationHolderResolveHandler implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return isSupportAnnotationClass(parameter) & isSupportParameterType(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null){
            throw new AuthenticationException("관리자 로그인 실패");
        }
        Member adminMember = (Member) session.getAttribute(ADMIN_SESSION_KEY);
        return new MemberPrincipal(adminMember);
    }

    private boolean isSupportParameterType(MethodParameter parameter) {
        return Principal.class.isAssignableFrom(parameter.getParameterType());
    }

    private boolean isSupportAnnotationClass(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AdminAuthenticationPrincipal.class);
    }
}
