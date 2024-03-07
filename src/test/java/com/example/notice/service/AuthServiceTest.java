package com.example.notice.service;


import com.example.notice.entity.Member;
import com.example.notice.exception.MemberNotExistException;
import com.example.notice.mock.auth.MockAuthProvider;
import com.example.notice.mock.repository.MockMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest {
    private AuthService authService = new AuthServiceImpl(new MockMemberRepository(), new MockAuthProvider());

    @Nested
    @DisplayName("로그인 서비스 테스트")
    public class LoginService {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Member member = MockMemberRepository.SAVED_MEMBER;
            //when
            String authentication = authService.userAuthentication(member);
            //then
            assertThat(authentication).isEqualTo(MockAuthProvider.AUTHENTICATION);
        }

        @DisplayName("회원가입된 회원이 아닐때 - (저장된 아이디가 없을때)")
        @Test
        public void notExistLoginId() throws Exception{
            //given
            Member member = Member.builder()
                    .loginId("id123")
                    .password("pw123")
                    .build();
            //when
            //then
            assertThatThrownBy(() -> authService.userAuthentication(member)).isInstanceOf(MemberNotExistException.class);
        }

        @DisplayName("회원의 비밀번호가 불일치 할때")
        @Test
        public void doesNotMatchPassword() throws Exception{
            //given
            Member member = Member.builder()
                    .loginId(MockMemberRepository.SAVED_MEMBER.getLoginId())
                    .password("pw123")
                    .build();
            //when
            //then
            assertThatThrownBy(() -> authService.userAuthentication(member)).isInstanceOf(MemberNotExistException.class);
        }
    }
}