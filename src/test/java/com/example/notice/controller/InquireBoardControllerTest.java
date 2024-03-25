package com.example.notice.controller;

import com.example.notice.constant.ResponseConstant;
import com.example.notice.entity.InquireBoard;
import com.example.notice.mock.util.MockMemberLogin;
import com.example.notice.service.InquireBoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

@WebMvcTest(InquireBoardController.class)
class InquireBoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InquireBoardService inquireBoardService;

    private final ObjectMapper mapper = new ObjectMapper();


    @Nested
    @DisplayName("문의 게시판 게시글 생성")
    public class InquireBoardCreateControllerTest {
        private static final String INQUIRE_BOARD_CREATE_URI = "/api/boards/inquire";

        @BeforeEach
        public void memberLogin() {
            Long memberId = 654153L;
            MockMemberLogin.memberLogin(memberId);
        }

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            InquireBoard inquireBoard = InquireBoard.builder()
                    .title("title")
                    .content("content")
                    .isSecret(true)
                    .build();

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(INQUIRE_BOARD_CREATE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(inquireBoard)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$." + ResponseConstant.INQUIRE_BOARD_ID_PARAM)
                            .exists());
        }

        @DisplayName("유효하지 않은 입력값이 들어올때")
        @ParameterizedTest
        @MethodSource("invalidInquireBoardParam")
        public void invalidParam(String title, String content, Boolean isSecret) throws Exception{
            //given
            InquireBoard inquireBoard = InquireBoard.builder()
                    .title(title)
                    .content(content)
                    .isSecret(isSecret)
                    .build();
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(INQUIRE_BOARD_CREATE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(inquireBoard)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        private static Stream<Arguments> invalidInquireBoardParam() {
            return Stream.of(
                    Arguments.of(null, "content", "true"), // null일때
                    Arguments.of("title", null, "false"),
                    Arguments.of("title", "content", null),
                    Arguments.of("", "content", "true"), // 빈 문자열일때
                    Arguments.of("title", "", "false")
            );
        }

    }


}