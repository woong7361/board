package com.example.notice.service;


import com.example.notice.auth.provider.AuthProvider;
import com.example.notice.entity.Member;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.exception.MemberNotExistException;
import com.example.notice.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest {

    MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    AuthProvider authProvider = Mockito.mock(AuthProvider.class);
    private AuthService authService = new AuthServiceImpl(
            memberRepository,
            authProvider);

    @Nested
    @DisplayName("로그인 서비스 테스트")
    public class LoginService {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Member member = Member.builder()
                    .loginId("loginId")
                    .password("password")
                    .build();

            String resultString = "result String";

            //when
            Mockito.when(memberRepository.findMemberByLoginIdAndPassword(member))
                    .thenReturn(Optional.of(member));

            Mockito.when(authProvider.createAuthentication(member))
                    .thenReturn(resultString);

            String authentication = authService.userAuthentication(member);

            //then
            Assertions.assertThat(authentication).isEqualTo(resultString);
        }

        @DisplayName("해당하는 회원이 없을때")
        @Test
        public void notExistMember() throws Exception{
            //given
            Member member = Member.builder()
                    .loginId("id123")
                    .password("pw123")
                    .build();

            //when
            Mockito.when(memberRepository.findMemberByLoginIdAndPassword(member))
                    .thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> authService.userAuthentication(member))
                    .isInstanceOf(MemberNotExistException.class);
        }
    }

    @Nested
    @DisplayName("로그인 아이디 중복 체크 테스트")
    public class LoginIdDuplicateCheckTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            String loginId = "loginId";

            //when
            Mockito.when(memberRepository.findMemberAndAdminByLoginId(loginId))
                    .thenReturn(Optional.empty());

            //then
            authService.checkDuplicateLoginId(loginId);
        }

        @DisplayName("중복되는 아이디가 존재할때")
        @Test
        public void duplicateLoginId() throws Exception{
            //given
            String duplicateLoginId = "loginId";

            //when
            Mockito.when(memberRepository.findMemberAndAdminByLoginId(duplicateLoginId))
                    .thenReturn(Optional.of(Member.builder().build()));

            //then
            Assertions.assertThatThrownBy(() -> authService.checkDuplicateLoginId(duplicateLoginId))
                    .isInstanceOf(BadRequestParamException.class);
        }
    }

    @Nested
    @DisplayName("관리자 인증 서비스")
    public class AdminAuthenticationTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Member adminMember = Member.builder()
                    .loginId("loginId")
                    .password("password")
                    .build();

            //when
            Mockito.when(memberRepository.findAdminMemberByLoginIdAndPassword(adminMember))
                    .thenReturn(Optional.of(adminMember));

            //then
            authService.adminAuthentication(adminMember);
        }

        @DisplayName("해당하는 관리자가 없을때")
        @Test
        public void notExistAdminMember() throws Exception {
            //given
            Member adminMember = Member.builder()
                    .loginId("loginId")
                    .password("password")
                    .build();

            //when
            Mockito.when(memberRepository.findAdminMemberByLoginIdAndPassword(adminMember))
                    .thenReturn(Optional.empty());

            //then
            Assertions.assertThatThrownBy(() -> authService.adminAuthentication(adminMember))
                    .isInstanceOf(MemberNotExistException.class);
        }
    }
}