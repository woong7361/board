package com.example.notice.auth;


import com.example.notice.auth.principal.Principal;

/**
 * 인증된 회원 보관소
 */
public class AuthenticationHolder {
    private static final ThreadLocal<Principal> threadLocal = ThreadLocal.withInitial(() -> null);


    /**
     * 인증된 회원 주입
     * @param principal 인증된 회원
     */
    public static void setPrincipal(Principal principal) {
        threadLocal.set(principal);
    }

    /**
     * 인증된 회원 가져오기
     * @return 인증된 회원
     */
    public static Principal getPrincipal() {
        return threadLocal.get();
    }

    /**
     * 회원 보관소 초기화
     */
    public static void clear() {
        threadLocal.remove();
    }
}
