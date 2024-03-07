package com.example.notice.mock.repository;

import com.example.notice.entity.Member;
import com.example.notice.repository.MemberRepository;

import java.util.Optional;

public class MockMemberRepository implements MemberRepository {

    public static Member SAVED_MEMBER = Member.builder()
            .memberId(30)
            .loginId("qwer")
            .password("asdf")
            .name("zxcv")
            .build();

    /**
     * @implSpec 아무런 행동을 하지 않음
     */
    @Override
    public void save(Member member) {

    }

    /**
     * @implSpec SAVED MEMBER와 이름이 같다면 참, 다르다면 거짓 반환
     */
    @Override
    public boolean isDuplicateMemberName(String memberName) {
        return SAVED_MEMBER.getName().equals(memberName);
    }

    /**
     * @implSpec SAVED MEMBER의 아이디와 비밀번호가 같다면 MEMBER 반환
     */
    @Override
    public Optional<Member> findMemberByLoginIdAndPassword(Member member) {
        if (SAVED_MEMBER.getLoginId().equals(member.getLoginId()) && SAVED_MEMBER.getPassword().equals(member.getPassword())) {
            return Optional.of(Member.builder()
                    .memberId(1)
                    .loginId(member.getLoginId())
                    .password(member.getPassword())
                    .name(member.getName())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findAdminMemberByLoginIdAndPassword(Member member) {
        return Optional.empty();
    }

}
