package com.example.notice.auth.principal;

import com.example.notice.entity.Member;

public class MemberPrincipal implements Principal<Member>{

    private Member member;

    @Override
    public Member getAuthentication() {
        return this.member;
    }

    public MemberPrincipal(Member member) {
        this.member = member;
    }
}
