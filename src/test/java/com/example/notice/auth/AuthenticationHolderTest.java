package com.example.notice.auth;

import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;


class AuthenticationHolderTest {

    @Nested
    @DisplayName("인증 객체 보관소 test")
    public class NestedClass {
        @DisplayName("인증 객체 보관 및 반환 테스트")
        @Test
        public void saveAndGet() throws Exception {
            //given
            Member member = Member.builder()
                    .memberId(1L)
                    .build();
            Principal<Member> principal = new MemberPrincipal(member);

            //when
            AuthenticationHolder.setPrincipal(principal);

            //then
            Principal<Member> savedPrincipal = AuthenticationHolder.getPrincipal();
            long savedMemberId = savedPrincipal.getAuthentication().getMemberId();
            assertThat(member.getMemberId()).isEqualTo(savedMemberId);
        }

        @DisplayName("인증 객체 보관소 비우기")
        @Test
        public void clearTest() throws Exception{
            //given
            Member member = Member.builder()
                    .memberId(1L)
                    .build();
            Principal<Member> principal = new MemberPrincipal(member);

            //when
            AuthenticationHolder.setPrincipal(principal);
            AuthenticationHolder.clear();

            //then
            assertThat(AuthenticationHolder.getPrincipal()).isEqualTo(null);
        }

        @DisplayName("로컬 스레드마다 다른 값 확인")
        @Test
        public void multiThread() throws Exception{
            //given
            ExecutorService executorService = Executors.newFixedThreadPool(100);

            int threadCount = 10000;
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Long> memberIds = new ArrayList<>();
            List<Long> results = new CopyOnWriteArrayList<>();

            //when
            for (long i = 0; i < threadCount; i++) {
                memberIds.add(i);

                Member member = Member.builder()
                        .memberId(i)
                        .build();
                Principal<Member> principal = new MemberPrincipal(member);

                executorService.submit(() -> {
                    try {
                        AuthenticationHolder.setPrincipal(principal);
                        Principal<Member> savedPrincipal = AuthenticationHolder.getPrincipal();
                        long savedMemberId = savedPrincipal.getAuthentication().getMemberId();

                        results.add(savedMemberId);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            memberIds.sort((t1, t2) -> (int) (t1 - t2));
            results.sort((t1, t2) -> (int) (t1 - t2));
            assertThat(memberIds).usingRecursiveComparison().isEqualTo(results);
        }

    }

}