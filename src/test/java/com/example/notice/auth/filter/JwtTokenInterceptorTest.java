package com.example.notice.auth.filter;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.path.AuthorizationRole;
import com.example.notice.auth.provider.AuthProvider;
import com.example.notice.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class JwtTokenInterceptorTest {

    AuthProvider authProvider = Mockito.mock(AuthProvider.class);
    JwtTokenInterceptor interceptor = new JwtTokenInterceptor(authProvider);

    @DisplayName("비회원이 들어올때")
    @Test
    public void nonMember() throws Exception{
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        //when
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn(null);
        Mockito.when(request.getMethod())
                .thenReturn("GET");

        interceptor.preHandle(request, response, null);

        //then
        Assertions.assertThat(AuthenticationHolder.getRole()).isEqualTo(AuthorizationRole.GUEST);
    }

    @DisplayName("회원이 들어올때")
    @Test
    public void member() throws Exception{
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        long memberId = 10L;
        String tokenString = "tokenString";

        //when
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn(tokenString);
        Mockito.when(authProvider.verify(tokenString))
                .thenReturn(Member.builder()
                        .memberId(memberId)
                        .build());
        Mockito.when(request.getMethod())
                .thenReturn("GET");

        interceptor.preHandle(request, response, null);

        //then
        Assertions.assertThat(AuthenticationHolder.getRole()).isEqualTo(AuthorizationRole.MEMBER);
    }

    @DisplayName("OPTIONS 메서드가 들어올때")
    @Test
    public void optionsMethod() throws Exception{
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        //when
        Mockito.when(request.getMethod())
                .thenReturn("OPTIONS");

        //then
        interceptor.preHandle(request, response, null);
    }

    @DisplayName("인증 객체가 dirty read가 되는지 확인")
    @Test
    public void isDirtyRead() throws Exception{
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        int threadCount = 5000;
        CountDownLatch latch = new CountDownLatch(threadCount);
        //when
        //then
        for (int i = 0; i < threadCount; i++) {
            String count = String.valueOf(i);

            executorService.submit(() -> {
                try {
                    // 항상 filter를 들어가기 전 인증객체 보관소는 null로 초기화 되어있어야 한다
                    Assertions.assertThat(AuthenticationHolder.getPrincipal()).isEqualTo(null);

                    Mockito.when(request.getMethod())
                            .thenReturn("GET");
                    Mockito.when(request.getHeader("Authorization"))
                            .thenReturn(count);
                    Mockito.when(authProvider.verify(count))
                            .thenReturn(Member.builder()
                                    .memberId(Long.valueOf(count))
                                    .build());

                    interceptor.preHandle(request, response, null);
                    interceptor.postHandle(request, response, null, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

}