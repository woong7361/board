package com.example.notice.auth.principal;

/**
 * 주요 인증 객체
 * @param <T> 인증 객체 타입
 */
public interface Principal<T> {

    /**
     * 인증객체 T를 가져온다.
     * @return 인증객체 T
     */
    public T getAuthentication();
}
