package com.example.notice.exception;

import com.example.notice.entity.Member;
import lombok.Getter;

/**
 * Member not exist exception
 */
@Getter
public class MemberNotExistException extends EntityNotExistException{
    private Member member;

    public MemberNotExistException(String message, long memberId) {
        super(message);
        this.member = Member.builder()
                .memberId(memberId)
                .build();
    }

    public MemberNotExistException(String message, String loginId, String password) {
        super(message);
        this.member = Member.builder()
                .loginId(loginId)
                .password(password)
                .build();

    }

    public MemberNotExistException(String message, Member member) {
        super(message);
        this.member = member;
    }
}
