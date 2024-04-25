package com.example.notice.auth.principal;

import com.example.notice.auth.path.AuthorizationRole;

/**
 * 주요 인증 객체
 * @param <T> 인증 객체 타입
 */
public interface Principal<T> {

    /**
     * 인증객체 T를 가져온다.
     * @return 인증객체 T
     */
    T getAuthentication();

    /**
     * 인증객체의 권한을 가져온다.
     * @return 권한 반환
     */
    AuthorizationRole getRole();
}
