package com.example.notice.auth;


import com.example.notice.auth.principal.Principal;

/**
 * 인증된 회원 보관소
 */
//TODO 인터페이스로 빼야 하는가? -> 맞는 말이다. 하지만 지금 굳이 할 필요가 있는가?
// 테스트 어렵지 않다, 외부에서 ThreadLocal 쓰는지 모른다. -> 인터페이스로 뺴는 것은 오버 엔지니어링?  나중에 필요하면 리팩토링 하는게 좋을듯 하다.

// TODO holder가 Member에 의존적이 상황 -> 서비스가 아닌 모듈에 엔티티가 의존적이라는 것은 매우 좋지 않은 신호 -> Holder에 wrapping 객체 추가?
// unwrapping 할때 캐스팅이 필요?
public class AuthenticationHolder {
    private static final ThreadLocal<Principal> threadLocal = ThreadLocal.withInitial(() -> null);


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

    private static boolean isExist() {
        return threadLocal.get() != null;
    }

}
