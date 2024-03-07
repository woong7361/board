package com.example.notice.controller;

import com.example.notice.entity.Member;
import com.example.notice.mock.auth.MockAuthProvider;
import com.example.notice.mock.service.MockAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    public static final String LOGIN_URI = "/api/auth/login";
    public static final String ADMIN_LOGIN_URI = "/admin/auth/login";

    @Autowired
    private MockMvc mockMvc;

    //TODO mockBean? 이미 controller test는 webMvcTest로 spring에 종속되어있는데 mocking 라이브러리의 도움을 받는편이 좋다?
    @SpyBean
    private MockAuthService authService;

    // WebMvcConfigure에서 필요
    @MockBean
    private MockAuthProvider authProvider;

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
            Member member = Member.builder()
                    .memberId(10)
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

            Assertions.assertThat(mockHttpSession.getAttribute(ADMIN_SESSION_KEY)).isEqualTo(10L);
        }

    }
}