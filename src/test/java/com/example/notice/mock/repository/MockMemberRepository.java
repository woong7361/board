package com.example.notice.mock.repository;

import com.example.notice.entity.Member;
import com.example.notice.repository.MemberRepository;
import org.opentest4j.TestAbortedException;

public class MockMemberRepository implements MemberRepository {

    public static String DUPLICATE_NAME = "dup";

    /**
     * @implSpec 아무런 행동을 하지 않음
     */
    @Override
    public void save(Member member) {

    }

    /**
     * @implSpec  memberName이 if("duplicate") -> true, else -> false
     */
    @Override
    public boolean isDuplicateMemberName(String memberName) {
        if (memberName.equals(DUPLICATE_NAME)) {
            return true;
        } else {
            return false;
        }

    }
}
