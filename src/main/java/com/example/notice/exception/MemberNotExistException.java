package com.example.notice.exception;

import com.example.notice.entity.Member;
import lombok.Getter;

/**
 * 회원이 존재하지 않는다는 에러 클래스
 */
@Getter
public class MemberNotExistException extends EntityNotExistException{
    private Member member;

    public MemberNotExistException(String message, Member member) {
        super(message);
        this.member = member;
    }
}
