package com.example.notice.auth;

import com.example.notice.auth.principal.Principal;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

//TODO handlerMethodArgumentResolver 없이 만들기?

/**
 * AuthenticationHolder 에서 인증객체를 가져오는 역할을 한다.
 */
@Component
public class AuthenticationHolderResolveHandler implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return isSupportAnnotationClass(parameter) & isSupportParameterType(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Principal principal = AuthenticationHolder.getPrincipal();
        if (principal == null) {
            throw new RuntimeException("no Authenticated Principal");
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
