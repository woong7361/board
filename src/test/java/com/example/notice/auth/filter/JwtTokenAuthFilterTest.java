package com.example.notice.auth.filter;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.mock.auth.MockAuthProvider;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class JwtTokenAuthFilterTest {

    Filter filter = new JwtTokenAuthFilter(new MockAuthProvider());

    @Disabled
    @Nested
    @DisplayName("인증 필터 테스트")
    public class AuthFilterTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
            ServletResponse response = Mockito.mock(MockHttpServletResponse.class);
            FilterChain filterChain = Mockito.mock(FilterChain.class);

            //when
            //then
            filter.doFilter(request, response, filterChain);
        }


        @Disabled
        @DisplayName("인증 객체가 dirty read가 되는지 확인")
        @Test
        public void isDirtyRead() throws Exception{
            //given
            HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
            ServletResponse response = new MockHttpServletResponse();
            FilterChain filterChain = Mockito.mock(FilterChain.class);

            ExecutorService executorService = Executors.newFixedThreadPool(100);

            int threadCount = 5000;
            CountDownLatch latch = new CountDownLatch(threadCount);
            //when
            //then
            for (int i = 0; i < threadCount; i++) {
                String temp = String.valueOf(i);

                executorService.submit(() -> {
                    try {
                        // 항상 filter를 들어가기 전 인증객체 보관소는 null로 초기화 되어있어야 한다
                        Assertions.assertThat(AuthenticationHolder.getPrincipal()).isEqualTo(null);

                        Mockito.when(request.getHeader("Authorization")).thenReturn(temp);
                        filter.doFilter(request, response, filterChain);
                    } catch (ServletException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

        }
    }

}