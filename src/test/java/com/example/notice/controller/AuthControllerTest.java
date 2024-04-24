package com.example.notice.controller;

import com.example.notice.entity.Member;
import com.example.notice.mock.config.NoFilterMvcTest;
import com.example.notice.mock.service.MockAuthService;
import com.example.notice.restdocs.RestDocsHelper;
import com.example.notice.service.AuthService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Stream;

import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends RestDocsHelper {

    @MockBean
    private MockAuthService authService;


    @BeforeEach
    public void initMapper() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    @Nested
    @DisplayName("일반 유저 로그인 테스트")
    public class LoginTest {
        public static final String LOGIN_URI = "/auth/member/login";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Member member = Member.builder()
                    .loginId("loginId153")
                    .password("password45")
                    .build();
            String tokenString = "tokenString";

            String body = mapper.writeValueAsString(member);

            //when
            Mockito.when(authService.userAuthentication(any(Member.class)))
                    .thenReturn(tokenString);

            ResultActions action = mockMvc.perform(
                    MockMvcRequestBuilders.post(LOGIN_URI)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON));

            //then
            action
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists())
                    .andExpect(jsonPath("$.token").value(tokenString))

            //rest docs
                    .andDo(restDocs.document(
                            PayloadDocumentation.requestFields(
                                    PayloadDocumentation.fieldWithPath("loginId").description("로그인 아이디"),
                                    PayloadDocumentation.fieldWithPath("password").description("비밀번호")
                            )))
                    .andDo(restDocs.document(
                            PayloadDocumentation.responseFields(
                                    PayloadDocumentation.fieldWithPath("token")
                                            .description("인증 토큰")
                            )));
        }

        @DisplayName("아이디 혹은 비밀번호가 null 일때")
        @ParameterizedTest
        @MethodSource("invalidLoginParam")
        public void nullableLoginIdOrPassword(String loginId, String password) throws Exception{
            //given
            Member nullLoginIdMember = Member.builder()
                    .loginId(loginId)
                    .password(password)
                    .build();

            String nullLoginIdBody = mapper.writeValueAsString(nullLoginIdMember);

            //when
            ResultActions action = mockMvc.perform(
                    MockMvcRequestBuilders.post(LOGIN_URI)
                            .content(nullLoginIdBody)
                            .contentType(MediaType.APPLICATION_JSON));

            //then
            action
                    .andExpect(
                            result -> Assertions.assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class))
                    .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> invalidLoginParam() {
            return Stream.of(
                    Arguments.of("loginId153", null),
                    Arguments.of(null, "password153")
            );
        }
    }

    @Nested
    @DisplayName("관리자 로그인 테스트")
    public class AdminLogin {
        public static final String ADMIN_LOGIN_URI = "/auth/admin/login";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Member requestMember = Member.builder()
                    .loginId("loginId12")
                    .password("password46")
                    .build();
            Member responseMember = Member.builder()
                    .memberId(48641L)
                    .name("name")
                    .build();

            Integer maxInactiveInterval = 1000;

            String body = mapper.writeValueAsString(requestMember);
            MockHttpSession mockHttpSession = new MockHttpSession();
            mockHttpSession.setMaxInactiveInterval(maxInactiveInterval);

            //when
            Mockito.when(authService.adminAuthentication(any(Member.class)))
                    .thenReturn(responseMember);

            ResultActions action = mockMvc.perform(
                    MockMvcRequestBuilders.post(ADMIN_LOGIN_URI)
                            .session(mockHttpSession)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON));

            //then
            action
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.memberId").value(responseMember.getMemberId()))
                    .andExpect(jsonPath("$.name").value(responseMember.getName()))
                    .andExpect(jsonPath("$.sessionTimeOut").value(maxInactiveInterval))

            //rest docs
                    .andDo(restDocs.document(
                            PayloadDocumentation.requestFields(
                                    PayloadDocumentation.fieldWithPath("loginId").description("로그인 아이디"),
                                    PayloadDocumentation.fieldWithPath("password").description("비밀번호")
                            )))
                    .andDo(restDocs.document(
                            PayloadDocumentation.responseFields(
                                    PayloadDocumentation.fieldWithPath("memberId").description("회원 식별자"),
                                    PayloadDocumentation.fieldWithPath("name").description("회원 이름"),
                                    PayloadDocumentation.fieldWithPath("sessionTimeOut").description("세션 만료시간")
                            )));

            Assertions.assertThat(mockHttpSession.getAttribute(ADMIN_SESSION_KEY))
                    .usingRecursiveComparison().isEqualTo(responseMember);
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
            Member loginId = Member.builder()
                    .loginId("loginId123")
                    .build();

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders.post(LOGIN_ID_DUPLICATE_CHECK_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(loginId)));

            //then
            action
                    .andExpect(status().isOk())

                    .andDo(restDocs.document(
                            PayloadDocumentation.requestFields(
                                    PayloadDocumentation.fieldWithPath("loginId").description("중복 확인할 로그인 아이디")
                            )));
        }
    }
}