package com.example.notice.mock.service;

import com.example.notice.entity.Member;
import com.example.notice.service.AuthService;

/**
 * mockAuth service
 */
public class MockAuthService implements AuthService {

    public static final String AUTHENTICATION = "authentication";

    /**
     * @implSpec login 성공시 인증 문자열을 반환한다. -> MockAuthService.AUTHENTICATION
     */
    @Override
    public String login(Member member) {
        return AUTHENTICATION;
//        return AUTHENTICATION;
    }
}
