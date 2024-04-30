package com.example.notice.auth.filter;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.path.AuthorizationRole;
import com.example.notice.auth.path.PathMethod;
import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.entity.Member;
import com.example.notice.exception.AuthorizationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.stream.Stream;


class AuthorizationInterceptorTest {

    AuthorizationInterceptor interceptor = new AuthorizationInterceptor();

    @BeforeEach
    public void init() {
        interceptor.includePathPatterns("/member/**", PathMethod.ANY, AuthorizationRole.MEMBER);
        interceptor.includePathPatterns("/guest/**", PathMethod.ANY, AuthorizationRole.GUEST);

        interceptor.excludePathPatterns("/member/exclude/**", PathMethod.ANY, AuthorizationRole.MEMBER);
        interceptor.excludePathPatterns("/guest/exclude/**", PathMethod.ANY, AuthorizationRole.GUEST);
    }

    @DisplayName("정상 처리")
    @ParameterizedTest
    @MethodSource("pathParams")
    public void success(String url, String method, AuthorizationRole role) throws Exception{
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setRequestURI(url);
        request.setMethod(method);
        AuthenticationHolder.setPrincipal(new MemberPrincipal(Member.builder().build(), role));

        //when
        //then
        interceptor.preHandle(request, response, null);

    }

    private static Stream<Arguments> pathParams() {
        return Stream.of(
                Arguments.of("/member/123", "GET", AuthorizationRole.MEMBER),
                Arguments.of("/member/123/123", "POST", AuthorizationRole.MEMBER),
                Arguments.of("/guest/123", "PUT", AuthorizationRole.GUEST),
                Arguments.of("/guest/123/123", "DELETE", AuthorizationRole.GUEST),
                Arguments.of("/guest/123", "PATCH", AuthorizationRole.GUEST)
        );
    }

    @DisplayName("권한이 부족한 요청이 올때")
    @ParameterizedTest
    @MethodSource("failPathParams")
    public void fail(String url, String method, AuthorizationRole role) throws Exception{
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setRequestURI(url);
        request.setMethod(method);
        AuthenticationHolder.setPrincipal(new MemberPrincipal(Member.builder().build(), role));

        //when
        //then
        Assertions.assertThatThrownBy(() -> interceptor.preHandle(request, response, null))
                .isInstanceOf(AuthorizationException.class);
    }

    private static Stream<Arguments> failPathParams() {
        return Stream.of(
                Arguments.of("/member/exclude", "GET", AuthorizationRole.MEMBER),
                Arguments.of("/member/exclude/123", "POST", AuthorizationRole.MEMBER),
                Arguments.of("/guest/exclude/", "PUT", AuthorizationRole.GUEST),
                Arguments.of("/guest/exclude/123/123", "DELETE", AuthorizationRole.GUEST),
                Arguments.of("/guest/exclude/123", "PATCH", AuthorizationRole.GUEST)
        );
    }

    @DisplayName("Options 메서드 처리")
    @Test
    public void optionsMethod() throws Exception{
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setRequestURI("/member/test");
        request.setMethod("OPTIONS");
        AuthenticationHolder.setPrincipal(new MemberPrincipal(Member.builder().build(), AuthorizationRole.MEMBER));

        //when
        //then
        interceptor.preHandle(request, response, null);


    }
}