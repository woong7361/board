package com.example.notice.mock.auth;

import com.example.notice.auth.JwtAuthProvider;
import com.example.notice.entity.Member;
import com.example.notice.auth.AuthProvider;

public class MockAuthProvider implements AuthProvider{

    public static final String AUTHENTICATION = "authentication";

    /**
     * @implSpec 인증 객체 반환
     */
    @Override
    public String createAuthentication(Member member) {
        return AUTHENTICATION;
    }

    /**
     * @implSpec 검증
     */
    @Override
    public Member verify(String authentication) {
        if (authentication == null) {
            return null;
        }

        return Member.builder()
                .memberId(Integer.parseInt(authentication))
                .build();
    }


}
