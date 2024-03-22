package com.example.notice.auth;

import com.example.notice.auth.principal.Principal;
import com.example.notice.exception.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * AuthenticationHolder 에서 인증객체를 가져오는 역할을 한다.
 */
@Component
public class AuthenticationHolderResolveHandler implements HandlerMethodArgumentResolver {

    /**
     * parameter가 Principal.class 인지 AND @Annotation이 AuthenticationPrincipal.class 인지
     * @param parameter method Argument parameter
     * @return 파라미터를 지원하는지
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return isSupportAnnotationClass(parameter) & isSupportParameterType(parameter);
    }

    /**
     * Authentication Holder에서 인증된 회원 객체를 꺼내준다.
     * @return 인증된 회원 객체
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Principal principal = AuthenticationHolder.getPrincipal();
        if (principal == null) {
            throw new AuthenticationException("인증과정이 처리되지 않았습니다.");
        }

        return principal;
    }

    private boolean isSupportParameterType(MethodParameter parameter) {
        return Principal.class.isAssignableFrom(parameter.getParameterType());
    }

    private boolean isSupportAnnotationClass(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

}
