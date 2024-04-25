package com.example.notice.auth.path;

public enum PathMethod {
    ANY,
    GET,
    POST,
    PUT,
    PATCH,
    DELETE;

    public static Boolean match(PathMethod method, PathMethod targetMethod) {
        if (ANY.equals(method)) {
            return true;
        }

        return method == targetMethod;
    }
}
