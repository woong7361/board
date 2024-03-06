package com.example.notice.service;

import com.example.notice.entity.Member;
import com.example.notice.exception.MemberNotExistException;
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
        //TODO 에러 메시지 상수화
        Member findMember = memberRepository.findMemberByLoginIdAndPassword(member)
                .orElseThrow(() -> new MemberNotExistException("해당하는 멤버 존재 X", member));

        return authProvider.createAuthentication(findMember);
    }
}
