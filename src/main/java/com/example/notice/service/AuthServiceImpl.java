package com.example.notice.service;

import com.example.notice.entity.Member;
import com.example.notice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{

    private final MemberRepository memberRepository;
    private final AuthProvider authProvider;

    @Override
    public String login(Member member) {
        Member findMember = memberRepository.findMemberByLoginIdAndPassword(member)
                .orElseThrow(() -> new IllegalArgumentException("not match!"));

        return authProvider.createAuthentication(findMember);
    }
}
