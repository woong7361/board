package com.example.notice.controller;

import com.example.notice.entity.InquireAnswer;
import com.example.notice.entity.Member;
import com.example.notice.mock.config.NoFilterMvcTest;
import com.example.notice.restdocs.RestDocsHelper;
import com.example.notice.service.InquireAnswerService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static com.example.notice.constant.ResponseConstant.INQUIRE_ANSWER_ID_PARAM;
import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@WebMvcTest(InquireAnswerController.class)
class InquireAnswerControllerTest extends RestDocsHelper {

    @MockBean
    InquireAnswerService inquireAnswerService;

    @BeforeEach
    public void initMapper() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Nested
    @DisplayName("문의게시판 문의 답변 생성 컨트롤러 테스트")
    public class InquireAnswerCreateControllerTest {

        private static final String INQUIRE_ANSWER_CREATE_URI = "/api/boards/inquire/{inquireBoardId}/answers";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long inquireBoardId = 15342L;
            long memberId = 418564L;

            InquireAnswer answer = InquireAnswer.builder()
                    .answer("fdsafdsa")
                    .build();

            MockHttpSession mockHttpSession = new MockHttpSession();
            mockHttpSession.setAttribute(ADMIN_SESSION_KEY, Member.builder()
                    .memberId(memberId)
                    .name("admin")
                    .build());

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders
                    .post(INQUIRE_ANSWER_CREATE_URI, inquireBoardId)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(memberId))
                    .session(mockHttpSession)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(answer)));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

                    .andDo(restDocs.document(
                            pathParameters(
                                    parameterWithName("inquireBoardId").description("문의 게시글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(
                            requestFields(
                                    fieldWithPath("answer").description("답변 내용")
                            )
                    ))
                    .andDo(restDocs.document(
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT token")
                            )
                    ));
        }

        @DisplayName("유효하지 않은 입력값이 들어올때")
        @ParameterizedTest
        @MethodSource("invalidInquireBoardParam")
        public void invalidParam(String answer) throws Exception{
            //given
            long inquireBoardId = 15342L;
            long memberId = 418564L;

            InquireAnswer body = InquireAnswer.builder()
                    .inquireAnswerId(415564L)
                    .answer(answer)
                    .build();

            MockHttpSession mockHttpSession = new MockHttpSession();
            mockHttpSession.setAttribute(ADMIN_SESSION_KEY, Member.builder()
                    .memberId(memberId)
                    .name("admin")
                    .build());
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(INQUIRE_ANSWER_CREATE_URI, inquireBoardId)
                            .headers(authenticationTestUtil.getLoginTokenHeaders(memberId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .session(mockHttpSession)
                            .content(mapper.writeValueAsString(body)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        private static Stream<Arguments> invalidInquireBoardParam() {
            return Stream.of(
                    Arguments.of(""),
                    Arguments.of("   ")
            );
        }

    }

    @Nested
    @DisplayName("문의 게시판 문의 답변 삭제 컨트롤러 테스트")
    public class InquireAnswerDeleteControllerTest {

        private static final String INQUIRE_ANSWER_DELETE_URI = "/api/boards/inquire/answers/{inquireAnswerId}";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long inquireAnswerId = 415341L;

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders
                    .delete(INQUIRE_ANSWER_DELETE_URI, inquireAnswerId)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(56352456L)));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

                    .andDo(restDocs.document(
                            pathParameters(
                                    parameterWithName("inquireAnswerId").description("답변 식별자")
                            )
                    ))
                    .andDo(restDocs.document(
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT token")
                            )
                    ));
        }
    }
}