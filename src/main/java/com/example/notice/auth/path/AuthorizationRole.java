package com.example.notice.auth.path;

/**
 * 인가 과정이 필요한 Role
 */
public enum AuthorizationRole {
    MEMBER, GUEST;

    /**
     * 권한을 비교한다.
     *
     * @param role 권한
     * @param targetRole 비교할 권한
     * @return 같다면 true / 다르다면 false 반환
     */
    public static boolean match(AuthorizationRole role, AuthorizationRole targetRole) {
        return role == targetRole;
    }
}
