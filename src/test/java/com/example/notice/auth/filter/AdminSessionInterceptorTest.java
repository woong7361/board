package com.example.notice.auth.filter;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.path.AuthorizationRole;
import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.constant.SessionConstant;
import com.example.notice.entity.Member;
import com.example.notice.exception.AuthenticationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;



class AdminSessionInterceptorTest {
    AdminSessionInterceptor interceptor = new AdminSessionInterceptor();

    @Nested
    @DisplayName("관리자 인증 확인 인터셉터 테스트")
    public class AdminInterceptorTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception{
            //given
            MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
            MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
            MockHttpSession mockHttpSession = new MockHttpSession();
            Member member = Member.builder().memberId(1L).build();

            mockHttpSession.setAttribute(SessionConstant.ADMIN_SESSION_KEY, member);
            mockHttpServletRequest.setSession(mockHttpSession);

            //when
            //then
            interceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null);
        }

        @DisplayName("세션이 없을때")
        @Test
        public void noSession() throws Exception {
            //given
            MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
            MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
            //when
            //then
            Assertions.assertThatThrownBy(() -> interceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null))
                    .isInstanceOf(AuthenticationException.class);

        }

        @DisplayName("세션에 저장된 객체가 없을때")
        @Test
        public void invalidSession() throws Exception {
            //given
            MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
            MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

            MockHttpSession mockHttpSession = new MockHttpSession();
            mockHttpServletRequest.setSession(mockHttpSession);
            //when
            //then
            Assertions.assertThatThrownBy(() -> interceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null))
                    .isInstanceOf(AuthenticationException.class);

        }

        @DisplayName("Options 메서드 처리")
        @Test
        public void optionsMethod() throws Exception{
            //given
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();

            request.setMethod("OPTIONS");
            AuthenticationHolder.setPrincipal(new MemberPrincipal(Member.builder().build(), AuthorizationRole.MEMBER));

            //when
            //then
            interceptor.preHandle(request, response, null);
        }

    }
}