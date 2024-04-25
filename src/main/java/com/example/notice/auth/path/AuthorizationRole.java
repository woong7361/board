package com.example.notice.auth.path;

public enum AuthorizationRole {
    MEMBER, GUEST;

    public static boolean match(AuthorizationRole role, AuthorizationRole targetRole) {
        return role == targetRole;
    }
}
