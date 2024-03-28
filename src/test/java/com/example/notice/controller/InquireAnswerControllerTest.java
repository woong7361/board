package com.example.notice.controller;

import com.example.notice.entity.InquireAnswer;
import com.example.notice.entity.Member;
import com.example.notice.mock.config.NoFilterMvcTest;
import com.example.notice.service.InquireAnswerService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static com.example.notice.constant.ResponseConstant.INQUIRE_ANSWER_ID_PARAM;
import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@NoFilterMvcTest(InquireAnswerController.class)
class InquireAnswerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InquireAnswerService inquireAnswerService;

    private final ObjectMapper mapper = new ObjectMapper();


    @Nested
    @DisplayName("문의게시판 문의 답변 생성 컨트롤러 테스트")
    public class InquireAnswerCreateControllerTest {

        private static final String INQUIRE_ANSWER_CREATE_URI = "/api/boards/inquire/%s/answers";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long inquireBoardId = 15342L;
            long memberId = 418564L;

            InquireAnswer answer = InquireAnswer.builder()
                    .inquireAnswerId(415564L)
                    .answer("fdsafdsa")
                    .build();

            MockHttpSession mockHttpSession = new MockHttpSession();
            mockHttpSession.setAttribute(ADMIN_SESSION_KEY, Member.builder()
                    .memberId(memberId)
                    .name("admin")
                    .build());

            //when
            Mockito.when(inquireAnswerService.createAnswer(any(), anyLong(), anyLong()))
                    .thenReturn(answer.getInquireAnswerId());

            //then
            mockMvc.perform(MockMvcRequestBuilders.post(INQUIRE_ANSWER_CREATE_URI.formatted(inquireBoardId))
                            .session(mockHttpSession)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(answer)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$." + INQUIRE_ANSWER_ID_PARAM)
                            .value(answer.getInquireAnswerId()));
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
            mockMvc.perform(MockMvcRequestBuilders.post(INQUIRE_ANSWER_CREATE_URI.formatted(inquireBoardId))
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

        private static final String INQUIRE_ANSWER_DELETE_URI = "/api/boards/inquire/answers/%s";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long inquireAnswerId = 415341L;

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.delete(INQUIRE_ANSWER_DELETE_URI.formatted(inquireAnswerId)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
}