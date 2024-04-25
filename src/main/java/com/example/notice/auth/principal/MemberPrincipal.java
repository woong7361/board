package com.example.notice.auth.principal;

import com.example.notice.auth.path.AuthorizationRole;
import com.example.notice.entity.Member;

public class MemberPrincipal implements Principal<Member>{

    private Member member;
    private AuthorizationRole role;

    @Override
    public Member getAuthentication() {
        return member;
    }

    @Override
    public AuthorizationRole getRole() {
        return role;
    }

    public MemberPrincipal(Member member) {
        this.member = member;
    }

    public MemberPrincipal(Member member, AuthorizationRole role) {
        this.member = member;
        this.role = role;
    }
}
