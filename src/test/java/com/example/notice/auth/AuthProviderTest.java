package com.example.notice.auth;

import com.example.notice.entity.Member;
import com.example.notice.mock.service.MockConfigurationService;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class AuthProviderTest {
    AuthProvider authProvider = new JwtAuthProvider(new MockConfigurationService());

    @Nested
    @DisplayName("인증 객체 테스트")
    public class NestedClass {
        @DisplayName("생성 및 반환")
        @Test
        public void success() throws Exception {
            //given
            Member member = Member.builder()
                    .memberId(1)
                    .name("name")
                    .build();
            //when
            String authentication = authProvider.createAuthentication(member);
            Member verifyMember = authProvider.verify(authentication);
            //then

            assertThat(member).usingRecursiveComparison().isEqualTo(verifyMember);
        }

        @DisplayName("오염된 인증 객체")
        @Test
        public void test() throws Exception{
            //given
            Member member = Member.builder()
                    .memberId(1)
                    .name("name")
                    .build();
            //when
            String authentication = authProvider.createAuthentication(member);
            String corruptedAuthentication = authentication + "123";

            //then
            assertThatThrownBy(() -> authProvider.verify(corruptedAuthentication)).isInstanceOf(SignatureException.class);
        }

    }
}