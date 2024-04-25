package com.example.notice.mock.auth;

import com.example.notice.entity.Member;
import com.example.notice.auth.provider.AuthProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MockAuthProvider implements AuthProvider{

    public static final String AUTHENTICATION = "authentication";

    public static final ObjectMapper mapper = new ObjectMapper();

    /**
     * @implSpec 인증 객체 반환
     */
    @Override
    public String createAuthentication(Member member) {
        try {
            return String.valueOf(mapper.writeValueAsString(member));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @implSpec 검증
     */
    @Override
    public Member verify(String authentication) {
        if (authentication == null) {
            return null;
        }

        try {
            return mapper.readValue(authentication, Member.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
