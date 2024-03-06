package com.example.notice.filter;

import com.example.notice.entity.Member;

/**
 * 인증된 회원 보관소
 */
public class AuthenticationHolder {
    private static final ThreadLocal<Member> threadLocal = ThreadLocal.withInitial(() -> null);

    public static void setMember(Member member) {
        threadLocal.set(member);
    }

    public static Long getMemberId() {
        if (isExist()) {
            return threadLocal.get().getMemberId();
        } else {
            return null;
        }
    }

    public static void clear() {
        threadLocal.remove();
    }

    private static boolean isExist() {
        return threadLocal.get() != null;
    }

}
