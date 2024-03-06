package com.example.notice.mock.auth;

import com.example.notice.entity.Member;
import com.example.notice.service.AuthProvider;

public class MockAuthProvider implements AuthProvider {

    public static final String AUTHENTICATION = "authentication";

    /**
     * @implSpec 인증 객체 반환
     */
    @Override
    public String createAuthentication(Member member) {
        return AUTHENTICATION;
    }
}
