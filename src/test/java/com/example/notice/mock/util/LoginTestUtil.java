package com.example.notice.mock.util;

import com.example.notice.entity.Member;
import jakarta.servlet.http.Cookie;
import org.springframework.mock.web.MockHttpSession;

import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;

public class LoginTestUtil {
    public static MockHttpSession getMockAdminSession(Long memberId) {
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(ADMIN_SESSION_KEY, Member.builder()
                .memberId(memberId)
                .build());

        return mockHttpSession;
    }

    public static MockHttpSession getMockAdminSession() {
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(ADMIN_SESSION_KEY, Member.builder()
                .memberId(7896412654L)
                .build());

        return mockHttpSession;
    }

    public static Cookie getMockSessionCookie() {
        return new Cookie("JSESSIONID", "ACBCDFD0FF93D5BB");
    }
}
