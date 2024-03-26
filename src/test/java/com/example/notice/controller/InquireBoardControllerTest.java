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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@WebMvcTest(InquireBoardController.class)
class InquireBoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InquireBoardService inquireBoardService;

    private final ObjectMapper mapper = new ObjectMapper();


    @Nested
    @DisplayName("문의 게시판 게시글 생성 컨트롤러 테스트")
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

    @Nested
    @DisplayName("문의 게시판 게시글 검색 컨트롤러 테스트")
    public class InquireBoardSearchControllerTest {

        private static final String INQUIRE_BOARD_SEARCH_URI = "/api/boards/inquire";
        @BeforeEach
        public void memberLogin() {
            Long memberId = 654153L;
            MockMemberLogin.memberLogin(memberId);
        }


        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("size", "5");
            params.add("currentPage", "0");
            params.add("keyWord", "4153");
            params.add("onlyMine", "false");
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.get(INQUIRE_BOARD_SEARCH_URI)
                            .params(params))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }


    @Nested
    @DisplayName("문의 게시판 게시글 조회 컨트롤러 테스트")
    public class InquireBoardGetControllerTest {
        private static final String GET_BOARD_CONTROLLER_URI = "/api/boards/inquire/%s";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long boardId = 1532423L;
            InquireBoard findBoard = InquireBoard.builder()
                    .inquireBoardId(boardId)
                    .title("title")
                    .content("content")
                    .isSecret(false)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .views(153452L)
                    .memberId(485641534L)
                    .build();

            //when
            Mockito.when(inquireBoardService.getBoardById(boardId))
                    .thenReturn(findBoard);
            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_BOARD_CONTROLLER_URI.formatted(boardId)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.inquireBoardId").value(boardId))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(findBoard.getContent()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(findBoard.getTitle()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.views").value(findBoard.getViews()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.memberId").value(findBoard.getMemberId()));

        }
    }

    @Nested
    @DisplayName("문의 게시판 게시글 수정 컨트롤러 테스트")
    public class InquireBoardUpdateControllerTest {

        private static final String UPDATE_INQUIRE_BOARD_URI = "/api/boards/inquire/%s";

        @BeforeEach
        public void memberLogin() {
            Long memberId = 654153L;
            MockMemberLogin.memberLogin(memberId);
        }

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long boardId = 1546743L;
            InquireBoard inputBoard = InquireBoard.builder()
                    .title("tiutle")
                    .content("consgsda")
                    .isSecret(false)
                    .build();

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_INQUIRE_BOARD_URI.formatted(boardId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(inputBoard)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @DisplayName("유효하지 않은 입력값이 들어올때")
        @ParameterizedTest
        @MethodSource("invalidInquireBoardParam")
        public void invalidParam(String title, String content, Boolean isSecret) throws Exception{
            //given
            long boardId = 1546743L;
            InquireBoard inquireBoard = InquireBoard.builder()
                    .title(title)
                    .content(content)
                    .isSecret(isSecret)
                    .build();
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_INQUIRE_BOARD_URI.formatted(boardId))
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

    @Nested
    @DisplayName("문의 게시판 게시글 삭제 컨트롤러 테스트")
    public class InquireBoardDeleteControllerTest {

        private static final String INQUIRE_BOARD_DELETE_URI = "/api/boards/inquire/%s";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long boardId = 41564L;

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.delete(INQUIRE_BOARD_DELETE_URI.formatted(boardId)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
}