package com.example.notice.mock.util;

import com.example.notice.auth.AuthProvider;
import com.example.notice.entity.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class AuthenticationTestUtil {

    public AuthenticationTestUtil(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    AuthProvider authProvider;

    public HttpHeaders getLoginTokenHeaders(Long memberId) {
        Member member = Member.builder()
                .memberId(1L)
                .build();

        String authentication = "Bearer " + authProvider.createAuthentication(member);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", authentication);

        return new HttpHeaders(headers);
    }
}
