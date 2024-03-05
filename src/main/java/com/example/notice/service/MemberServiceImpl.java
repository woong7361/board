package com.example.notice.service;

import com.example.notice.entity.Member;
import com.example.notice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createUserRoleMember(Member member) {
        member.setUserRole();
        memberRepository.save(member);
    }

    public void isDuplicateMemberName(String memberName) {
        if (memberRepository.isDuplicateMemberName(memberName)) {
            // TODO custom exception 필요
            throw new IllegalArgumentException("duplicate member name");
        }
    }


}
