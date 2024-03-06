package com.example.notice.auth;

import com.example.notice.entity.Member;
import org.assertj.core.api.Assertions;
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
                    .memberId(1)
                    .build();
            //when
            AuthenticationHolder.setMember(member);
            Long savedMemberId = AuthenticationHolder.getMemberId();
            //then

            assertThat(member.getMemberId()).isEqualTo(savedMemberId);
        }

        @DisplayName("인증 객체 보관소 비우기")
        @Test
        public void clearTest() throws Exception{
            //given
            Member member = Member.builder()
                    .memberId(1)
                    .build();
            //when
            AuthenticationHolder.setMember(member);
            AuthenticationHolder.clear();
            //then
            assertThat(AuthenticationHolder.getMemberId()).isEqualTo(null);
        }

        //TODO 테스트 실패 - why?
        @DisplayName("로컬 스레드마다 다른 값 확인")
        @Test
        public void multiThread() throws Exception{
            //given
            ExecutorService executorService = Executors.newFixedThreadPool(100);

            int threadCount = 10000;
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Long> memberIds = new ArrayList<>();

            // add 작업만 하는데 데이터가 누락될 이유?
            // add 작업이 atomic 하지 않아 발생한 문제? -> add도 atomic 해야 하는가? -> 그렇다고 볼 수 있다.
//            List<Long> results = new ArrayList<>();
            List<Long> results = new CopyOnWriteArrayList<>();

            //when
            for (int i = 0; i < threadCount; i++) {
                memberIds.add((long) i);

                Member member = Member.builder()
                        .memberId(i)
                        .build();

                executorService.submit(() -> {
                    try {
                        AuthenticationHolder.setMember(member);
                        results.add(AuthenticationHolder.getMemberId());
//                        System.out.println(member.getMemberId() + "    " + AuthenticationHolder.getMemberId() + "  " + (member.getMemberId() == AuthenticationHolder.getMemberId()));
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