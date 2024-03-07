package com.example.notice.mock.repository;

import com.example.notice.entity.Member;
import com.example.notice.entity.MemberRole;
import com.example.notice.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MockMemberRepository implements MemberRepository {

    private static final List<Member> repository = new ArrayList<>();

    public static Member SAVED_MEMBER = Member.builder()
            .memberId(30L)
            .loginId("qwer21")
            .password("asdf21")
            .name("zxcv")
            .role(MemberRole.USER)
            .build();

    public static Member SAVED_ADMIN_MEMBER = Member.builder()
            .memberId(20L)
            .loginId("abcd12")
            .password("efdg12")
            .name("hjkl")
            .role(MemberRole.ADMIN)
            .build();

    static {
        repository.add(SAVED_MEMBER);
        repository.add(SAVED_ADMIN_MEMBER);
    }

    /**
     * @implSpec 아무런 행동을 하지 않음
     */
    @Override
    public void save(Member member) {
        repository.add(member);
    }

    /**
     * @implSpec SAVED MEMBER와 이름이 같다면 참, 다르다면 거짓 반환
     */
    @Override
    public boolean isDuplicateMemberName(String memberName) {
        return repository.stream()
                .anyMatch((member) -> member.getName().equals(memberName));
    }

    /**
     * @implSpec SAVED MEMBER의 아이디와 비밀번호가 같다면 MEMBER 반환
     */
    @Override
    public Optional<Member> findMemberByLoginIdAndPassword(Member member) {
        return repository.stream()
                .filter((savedMember) -> {
                    return savedMember.getLoginId().equals(member.getLoginId()) &&
                            savedMember.getPassword().equals(member.getPassword()) &&
                            savedMember.getRole().equals(MemberRole.USER);
                })
                .findFirst();
    }

    @Override
    public Optional<Member> findAdminMemberByLoginIdAndPassword(Member member) {
        return repository.stream()
                .filter((savedMember) -> {
                    return savedMember.getLoginId().equals(member.getLoginId()) &&
                            savedMember.getPassword().equals(member.getPassword()) &&
                            savedMember.getRole().equals(MemberRole.ADMIN);
                })
                .findFirst();
    }

}
