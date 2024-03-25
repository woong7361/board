package com.example.notice.mock.util;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.entity.Member;

public class MockMemberLogin {

    public static void memberLogin(Long memberId) {
        Member loginMember = Member.builder()
                .memberId(memberId)
                .build();

        AuthenticationHolder.setPrincipal(new MemberPrincipal(loginMember));
    }
}
