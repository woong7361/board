package com.example.notice.service;

import com.example.notice.entity.Member;
import com.example.notice.entity.MemberRole;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;


class MemberServiceTest {

    MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    MemberService memberService = new MemberServiceImpl(memberRepository);


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
            Mockito.when(memberRepository.isDuplicateMemberLoginId(member.getLoginId()))
                    .thenReturn(false);

            memberService.createUserRoleMember(member);

            //then
            assertThat(member.getRole()).isEqualTo(MemberRole.USER);
        }

        @DisplayName("로그인 아이디가 중복일때")
        @Test
        public void duplicateLoginId() throws Exception{
            //given
            Member member = Member.builder()
                    .loginId("abc123")
                    .password("bdf123")
                    .name("name")
                    .build();

            //when
            Mockito.when(memberRepository.isDuplicateMemberLoginId(member.getLoginId()))
                    .thenReturn(true);

            //then
            assertThatThrownBy(() -> memberService.createUserRoleMember(member))
                    .isInstanceOf(BadRequestParamException.class);
        }
    }




}