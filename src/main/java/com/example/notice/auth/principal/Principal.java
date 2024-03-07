package com.example.notice.auth.principal;

/**
 * 주요 인증 객체
 * @param <T> 인증 객체 타입
 */
public interface Principal<T> {

    public T getAuthentication();
}
