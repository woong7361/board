package com.example.notice.auth;

import com.example.notice.auth.provider.AuthProvider;
import com.example.notice.auth.provider.JwtAuthProvider;
import com.example.notice.config.ConfigurationService;
import com.example.notice.entity.Member;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class AuthProviderTest {
    ConfigurationService configurationService = Mockito.mock(ConfigurationService.class);
    AuthProvider authProvider = new JwtAuthProvider(configurationService);

    private static final String SECRET_KEY =
            "fdsajfkdlsajhvkcnbjgfdsiojglf1523423151342s3df1324f3ds1gfgd415gf4ds3g15fd3s41gfdgf1d5341";


    @Nested
    @DisplayName("인증 객체 테스트")
    public class NestedClass {
        @DisplayName("암호화 및 복호화")
        @Test
        public void success() throws Exception {
            //given
            Member member = Member.builder()
                    .memberId(1L)
                    .name("name")
                    .build();

            long duration = 60L;

            //when
            Mockito.when(configurationService.getJwtDuration())
                    .thenReturn(duration);
            Mockito.when(configurationService.getSecretKey())
                    .thenReturn(SECRET_KEY);

            String authentication = authProvider.createAuthentication(member);
            Member verifyMember = authProvider.verify(authentication);
            //then

            assertThat(member).usingRecursiveComparison().isEqualTo(verifyMember);
        }

        @DisplayName("오염된 인증 객체")
        @Test
        public void corruptedAuthenticationObject() throws Exception{
            //given
            Member member = Member.builder()
                    .memberId(1L)
                    .name("name")
                    .build();

            //when
            Mockito.when(configurationService.getJwtDuration())
                    .thenReturn(10L);
            Mockito.when(configurationService.getSecretKey())
                    .thenReturn(SECRET_KEY);

            String authentication = authProvider.createAuthentication(member);
            String corruptedAuthentication = authentication + "123";

            //then
            assertThatThrownBy(() -> authProvider.verify(corruptedAuthentication))
                    .isInstanceOf(SignatureException.class);
        }

        @DisplayName("JWT가 만료되었을때")
        @Test
        public void expiredJWT() throws Exception{
            //given
            Member member = Member.builder()
                    .memberId(1L)
                    .name("name")
                    .build();

            //when
            Mockito.when(configurationService.getJwtDuration())
                    .thenReturn(0L);
            Mockito.when(configurationService.getSecretKey())
                    .thenReturn(SECRET_KEY);

            String authentication = authProvider.createAuthentication(member);

            //then
            assertThatThrownBy(() -> authProvider.verify(authentication))
                    .isInstanceOf(ExpiredJwtException.class);
        }

    }
}