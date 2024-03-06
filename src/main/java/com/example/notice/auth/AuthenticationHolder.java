package com.example.notice.auth;

import com.example.notice.entity.Member;

/**
 * 인증된 회원 보관소
 */
//TODO 인터페이스로 빼야 하는가? -> 맞는 말이다. 하지만 지금 굳이 할 필요가 있는가?
// 테스트 어렵지 않다, 외부에서 ThreadLocal 쓰는지 모른다. -> 인터페이스로 뺴는 것은 오버 엔지니어링?  나중에 필요하면 리팩토링 하는게 좋을듯 하다.
public class AuthenticationHolder {
    private static final ThreadLocal<Member> threadLocal = ThreadLocal.withInitial(() -> null);

    /**
     * 인증된 회원 보관
     * @param member 인증된 회원
     */
    public static void setMember(Member member) {
        threadLocal.set(member);
    }

    /**
     * 인증된 회원 식별자 가져오기
     * @return 회원 식별자
     */
    public static Long getMemberId() {
        if (isExist()) {
            return threadLocal.get().getMemberId();
        } else {
            return null;
        }
    }

    /**
     * 회원 보관소 초기화
     */
    public static void clear() {
        threadLocal.remove();
    }

    private static boolean isExist() {
        return threadLocal.get() != null;
    }

}
