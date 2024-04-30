package com.example.notice.mock.service;

import com.example.notice.entity.Member;
import com.example.notice.service.MemberService;

/**
 * MemberService Mock 객체
 */
public class MockMemberService implements MemberService {

    /**
     * @implSpec 아무런 행동을 하지 않음
     */
    @Override
    public void createUserRoleMember(Member member) {
    }
}
