package com.example.notice.service;

import com.example.notice.entity.Member;
import com.example.notice.entity.MemberRole;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.mock.repository.MockMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class MemberServiceTest {
    MemberService memberService = new MemberServiceImpl(new MockMemberRepository());


    @BeforeEach
    public void clearRepository() {
        MemoryDataBase.initRepository();
    }
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

        @DisplayName("로그인 아이디가 중복일때")
        @Test
        public void duplicateLoginId() throws Exception{
            //given
            Member member = Member.builder()
                    .loginId("abc123")
                    .password("bdf123")
                    .name(MockMemberRepository.SAVED_MEMBER.getName())
                    .build();
            //when
            memberService.createUserRoleMember(member);
            // then
            assertThatThrownBy(() -> memberService.createUserRoleMember(member))
                    .isInstanceOf(BadRequestParamException.class);
        }
    }




}