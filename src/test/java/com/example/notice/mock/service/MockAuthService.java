package com.example.notice.mock.service;

import com.example.notice.entity.Member;
import com.example.notice.service.AuthService;

/**
 * mockAuth service
 */
public class MockAuthService implements AuthService {

    public static final String AUTHENTICATION = "authentication";

    @Override
    public String userAuthentication(Member member) {
        return AUTHENTICATION;
    }

    @Override
    public Member adminAuthentication(Member member) {
        return member;
    }

    @Override
    public void checkDuplicateLoginId(String loginId) {
    }
}
