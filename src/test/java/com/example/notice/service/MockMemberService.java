package com.example.notice.service;

import com.example.notice.entity.Member;

/**
 * MemberService Mock 객체
 */
public class MockMemberService implements MemberService{

    @Override
    public void createUserRoleMember(Member member) {
    }

    @Override
    public void isDuplicateMemberName(String memberName) {
    }
}
