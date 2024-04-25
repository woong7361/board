package com.example.notice.auth.path;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class AuthorizationRoleTest {

    @DisplayName("Authorization Role 비교 테스트")
    @Test
    public void matchTest() throws Exception{
        //given
        //when
        boolean matchResult1 = AuthorizationRole.match(AuthorizationRole.MEMBER, AuthorizationRole.MEMBER);
        boolean matchResult2 = AuthorizationRole.match(AuthorizationRole.GUEST, AuthorizationRole.GUEST);
        boolean nonMatchResult = AuthorizationRole.match(AuthorizationRole.MEMBER, AuthorizationRole.GUEST);

        //then
        Assertions.assertThat(matchResult1).isTrue();
        Assertions.assertThat(matchResult2).isTrue();
        Assertions.assertThat(nonMatchResult).isFalse();
    }

}