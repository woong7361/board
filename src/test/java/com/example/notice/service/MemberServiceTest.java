package com.example.notice.service;

import com.example.notice.entity.Member;
import com.example.notice.entity.MemberRole;
import com.example.notice.mock.repository.MockMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class MemberServiceTest {
    MemberService memberService = new MemberServiceImpl(new MockMemberRepository());


    @Nested
    @DisplayName("일반(USER) 회원 생성 서비스 테스트")
    public class CreateMemberTest {
        @DisplayName("정상 동작")
        @Test
        public void success() throws Exception {
            //given
            Member member = Member.builder()
                    .loginId("abc123")
                    .password("bdf123")
                    .name("name")
                    .build();
            //when
            memberService.createUserRoleMember(member);
            //then
            assertThat(member.getRole()).isEqualTo(MemberRole.USER);
        }

        @DisplayName("이름이 중복일때")
        @Test
        public void duplicateName() throws Exception{
            //given
            Member member = Member.builder()
                    .loginId("abc123")
                    .password("bdf123")
                    .name(MockMemberRepository.SAVED_MEMBER.getName())
                    .build();
            // then
            assertThatThrownBy(() -> memberService.createUserRoleMember(member))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }




}