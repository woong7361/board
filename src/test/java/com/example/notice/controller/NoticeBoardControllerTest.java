package com.example.notice.controller;

import com.example.notice.entity.Member;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.service.NoticeBoardService;
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
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Stream;

import static com.example.notice.constant.ResponseConstant.FIXED_NOTICE_BOARDS_PARAM;
import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(NoticeBoardController.class)
class NoticeBoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    NoticeBoardService noticeBoardService;

    private ObjectMapper mapper = new ObjectMapper();

    @Nested
    @DisplayName("알림 게시판 게시글 생성 컨트롤러 테스트")
    public class NoticeBoardCreate {
        private static final String NOTICE_BOARD_CREATE_URI = "/admin/boards/notice";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            NoticeBoard board = NoticeBoard.builder()
                    .category("category")
                    .title("title")
                    .content("content")
                    .isFixed(true)
                    .build();

            MockHttpSession mockHttpSession = new MockHttpSession();
            mockHttpSession.setAttribute(ADMIN_SESSION_KEY, Member.builder().build());

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(NOTICE_BOARD_CREATE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(board))
                            .session(mockHttpSession))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @DisplayName("요청 인자 테스트")
        @ParameterizedTest
        @MethodSource("invalidInputs")
        public void test(String category, String title, String content, Boolean isFixed) throws Exception{
            //given
            NoticeBoard board = NoticeBoard.builder()
                    .category(category)
                    .title(title)
                    .content(content)
                    .isFixed(isFixed)
                    .build();

            MockHttpSession mockHttpSession = new MockHttpSession();
            mockHttpSession.setAttribute(ADMIN_SESSION_KEY, Member.builder().build());
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(NOTICE_BOARD_CREATE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(board))
                            .session(mockHttpSession))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> assertThat(result.getResolvedException())
                            .isInstanceOf(MethodArgumentNotValidException.class));
        }

        public static Stream<Arguments> invalidInputs() {
            return Stream.of(
                    Arguments.of(null, "title", "content", "true"), // null일떄
                    Arguments.of("category", null, "content", "true"),
                    Arguments.of("category", "title", null, "true"),
                    Arguments.of("category", "title", "", null),
                    Arguments.of(" ", "title", "content", false), //빈칸 일때
                    Arguments.of("category", " ", "conetent", false),
                    Arguments.of("category", "title", "", false));
        }
    }

    @Nested
    @DisplayName("알림 게시판 상단 고정글 가져오기 컨트롤러 테스트")
    public class GetFixedNoticeBoard {
        private static final String GET_FIXED_NOTICE_BOARD_URI = "/api/boards/notice/fixed";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            NoticeBoard board = NoticeBoard.builder()
                    .noticeBoardId(156L)
                    .title("title1")
                    .category("category1")
                    .views(123L)
                    .isFixed(true)
                    .memberId(1531L)
                    .build();
            //when
            Mockito.when(noticeBoardService.getFixedNoticeBoardWithoutContent())
                    .thenReturn(List.of(board));

            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_FIXED_NOTICE_BOARD_URI))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.%s[0].noticeBoardId".formatted(FIXED_NOTICE_BOARDS_PARAM)).value(board.getNoticeBoardId()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.%s[0].category".formatted(FIXED_NOTICE_BOARDS_PARAM)).value(board.getCategory()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.%s[0].title".formatted(FIXED_NOTICE_BOARDS_PARAM)).value(board.getTitle()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.%s[0].createdAt".formatted(FIXED_NOTICE_BOARDS_PARAM)).value(board.getCreatedAt()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.%s[0].isFixed".formatted(FIXED_NOTICE_BOARDS_PARAM)).value(board.getIsFixed()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.%s[0].memberId".formatted(FIXED_NOTICE_BOARDS_PARAM)).value(board.getMemberId()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.%s[0].memberName".formatted(FIXED_NOTICE_BOARDS_PARAM)).value(board.getMemberName()));
        }

        @DisplayName("여러개 있을때")
        @Test
        public void multipleResult() throws Exception{
            //given
            NoticeBoard board1 = NoticeBoard.builder()
                    .noticeBoardId(156L)
                    .build();
            NoticeBoard board2 = NoticeBoard.builder()
                    .noticeBoardId(157L)
                    .build();
            NoticeBoard board3 = NoticeBoard.builder()
                    .noticeBoardId(158L)
                    .build();
            //when
            Mockito.when(noticeBoardService.getFixedNoticeBoardWithoutContent())
                    .thenReturn(List.of(board1, board2, board3));

            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_FIXED_NOTICE_BOARD_URI))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.%s[0].noticeBoardId".formatted(FIXED_NOTICE_BOARDS_PARAM)).value(board1.getNoticeBoardId()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.%s[1].noticeBoardId".formatted(FIXED_NOTICE_BOARDS_PARAM)).value(board2.getNoticeBoardId()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.%s[2].noticeBoardId".formatted(FIXED_NOTICE_BOARDS_PARAM)).value(board3.getNoticeBoardId()));

        }
    }
}