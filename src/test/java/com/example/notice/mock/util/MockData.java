package com.example.notice.mock.util;

import com.example.notice.entity.Member;
import com.example.notice.entity.MemberRole;

public class MockData {

    public static Member MEMBER = Member.builder()
            .memberId(98756434566L)
            .loginId("loginId123")
            .password("password123")
            .role(MemberRole.USER)
            .build();
}
