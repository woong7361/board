package com.example.notice.service;

import com.example.notice.entity.Member;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.notice.constant.ErrorMessageConstant.DUPLICATE_NAME_MESSAGE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createUserRoleMember(Member member) {
        isDuplicateMemberLoginId(member.getLoginId());

        member.setUserRole();
        memberRepository.save(member);
    }

    public void isDuplicateMemberLoginId(String loginId) {
        if (memberRepository.isDuplicateMemberLoginId(loginId)) {
            throw new BadRequestParamException(DUPLICATE_NAME_MESSAGE);
        }
    }


}
