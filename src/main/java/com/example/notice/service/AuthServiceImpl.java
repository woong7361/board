package com.example.notice.service;

import com.example.notice.auth.AuthProvider;
import com.example.notice.entity.Member;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.exception.MemberNotExistException;
import com.example.notice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.notice.constant.ErrorMessageConstant.DUPLICATE_LOGIN_ID_MESSAGE;
import static com.example.notice.constant.ErrorMessageConstant.MEMBER_NOT_EXIST_MESSAGE;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{

    private final MemberRepository memberRepository;
    private final AuthProvider authProvider;

    @Override
    public String userAuthentication(Member member) {
        Member findMember = memberRepository.findMemberByLoginIdAndPassword(member)
                .orElseThrow(() -> new MemberNotExistException(MEMBER_NOT_EXIST_MESSAGE, member));

        return authProvider.createAuthentication(findMember);
    }

    @Override
    public Member adminAuthentication(Member member) {
        return memberRepository.findAdminMemberByLoginIdAndPassword(member)
                .orElseThrow(() -> new MemberNotExistException(MEMBER_NOT_EXIST_MESSAGE, member));
    }

    @Override
    public void checkDuplicateLoginId(String loginId) {
        Optional<Member> findMember = memberRepository.findMemberByLoginId(loginId);

        if (findMember.isPresent()) {
            throw new BadRequestParamException(DUPLICATE_LOGIN_ID_MESSAGE);
        }
    }
}
