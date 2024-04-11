package com.example.notice.controller;

import com.example.notice.entity.Member;
import com.example.notice.mock.config.NoFilterMvcTest;
import com.example.notice.mock.service.MockAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;

@NoFilterMvcTest(AuthController.class)
class AuthControllerTest {
    public static final String LOGIN_URI = "/auth/member/login";
    public static final String ADMIN_LOGIN_URI = "/auth/admin/login";

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private MockAuthService authService;

    private ObjectMapper mapper = new ObjectMapper();

    @Nested
    @DisplayName("일반 유저 로그인 테스트")
    public class LoginTest {

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Member member = Member.builder()
                    .loginId("id123")
                    .password("pw123")
                    .build();
            String body = mapper.writeValueAsString(member);
            //when
            //then

            mockMvc.perform(
                            MockMvcRequestBuilders.post(LOGIN_URI)
                                    .content(body)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(MockAuthService.AUTHENTICATION));
        }

        @DisplayName("아이디 혹은 비밀번호가 null 일때")
        @Test
        public void nullableLoginIdOrPassword() throws Exception{
            //given
            Member nullLoginIdMember = Member.builder()
                    .loginId(null)
                    .password("pw123")
                    .build();
            String nullLoginIdBody = mapper.writeValueAsString(nullLoginIdMember);
            //when
            //then
            mockMvc.perform(
                            MockMvcRequestBuilders.post(LOGIN_URI)
                                    .content(nullLoginIdBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(
                            result -> Assertions.assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class)
                    )
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

            //given
            Member nullPasswordMember = Member.builder()
                    .loginId(null)
                    .password("pw123")
                    .build();
            String nullPasswordBody = mapper.writeValueAsString(nullPasswordMember);
            //when
            //then
            mockMvc.perform(
                            MockMvcRequestBuilders.post(LOGIN_URI)
                                    .content(nullPasswordBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(
                            result -> Assertions.assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class)
                    )
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("관리자 로그인 테스트")
    public class AdminLogin {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long memberId = 10L;
            Member member = Member.builder()
                    .memberId(memberId)
                    .loginId("id123")
                    .password("pw123")
                    .build();
            String body = mapper.writeValueAsString(member);
            MockHttpSession mockHttpSession = new MockHttpSession();

            //when
            //then
            mockMvc.perform(
                            MockMvcRequestBuilders.post(ADMIN_LOGIN_URI)
                                    .session(mockHttpSession)
                                    .content(body)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            Assertions.assertThat(mockHttpSession.getAttribute(ADMIN_SESSION_KEY))
                    .usingRecursiveComparison().isEqualTo(member);
        }

    }

    @Nested
    @DisplayName("로그인 아이디 중복 확인 테스트")
    public class LoginIdDuplicateControllerTest {

        private static final String LOGIN_ID_DUPLICATE_CHECK_URI = "/auth/member/login-id";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            String loginId = "loginId";

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_ID_DUPLICATE_CHECK_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(loginId)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
}