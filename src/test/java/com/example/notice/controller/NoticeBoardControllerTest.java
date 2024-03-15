package com.example.notice.controller;

import com.example.notice.constant.ResponseConstant;
import com.example.notice.constant.SessionConstant;
import com.example.notice.dto.NoticeBoardSearchParam;
import com.example.notice.entity.Member;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.mock.repository.MockNoticeBoardRepository;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.service.NoticeBoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.example.notice.constant.ResponseConstant.FIXED_NOTICE_BOARDS_PARAM;
import static com.example.notice.constant.ResponseConstant.NONE_FIXED_NOTICE_BOARDS_PARAM;
import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;
import static com.example.notice.mock.repository.MockNoticeBoardRepository.NO_FK_NOTICE_BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(NoticeBoardController.class)
class NoticeBoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    NoticeBoardService noticeBoardService;

    private ObjectMapper mapper = new ObjectMapper();

    @Nested
    @DisplayName("공지 게시판 게시글 생성 컨트롤러 테스트")
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
    @DisplayName("공지 게시판 상단 고정글 가져오기 컨트롤러 테스트")
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


    @Nested
    @DisplayName("상단 고정 공지를 제외한 공지 검색 컨트롤러 테스트")
    public class GetNoneFixedNoticeTest {
        private static final String GET_NONE_FIXED_NOTICE_BOARDS_URI = "/api/boards/notice";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", LocalDateTime.now().minusMonths(2L).toString());
            params.add("endDate", LocalDateTime.now().toString());
            params.add("category", "category");
            params.add("keyWord", "keyWord");
            params.add("size", "5");
            params.add("currentPage", "0");
            //when

            int totalCount = 123;
            PageRequest pageRequest = new PageRequest(5, 0, null, null);
            List<NoticeBoard> noticeBoards = List.of(NO_FK_NOTICE_BOARD);
            Mockito.when(noticeBoardService.getNoneFixedNoticeBoards(any(NoticeBoardSearchParam.class), any(PageRequest.class)))
                    .thenReturn(
                            new PageResponse<>(
                                    noticeBoards,
                                    pageRequest,
                                    totalCount));

            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_NONE_FIXED_NOTICE_BOARDS_URI)
                            .params(params))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.%s[0].noticeBoardId".formatted("contents"))
                            .value(NO_FK_NOTICE_BOARD.getNoticeBoardId()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.%s[0].category".formatted("contents"))
                            .value(NO_FK_NOTICE_BOARD.getCategory()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.%s[0].title".formatted("contents"))
                            .value(NO_FK_NOTICE_BOARD.getTitle()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.%s[0].views".formatted("contents"))
                            .value(NO_FK_NOTICE_BOARD.getViews()))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.%s[0].createdAt".formatted("contents"))
//                            .value(NO_FK_NOTICE_BOARD.getCreatedAt()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.%s[0].memberName".formatted("contents"))
                            .value(NO_FK_NOTICE_BOARD.getMemberName()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.%s[0].memberId".formatted("contents"))
                            .value(NO_FK_NOTICE_BOARD.getMemberId()))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @DisplayName("정렬 인자에 특수문자가 들어갈때")
        @ParameterizedTest
        @MethodSource("specialWordSortParams")
        public void specialWordInSortParam(String orderColumn, String orderType) throws Exception{
            //given
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("orderColumn", orderColumn);
            params.add("orderType", orderType);
            params.add("size", "5");
            params.add("currentPage", "0");

            //when
            int totalCount = 123;
            PageRequest pageRequest = new PageRequest(5, 0, null, null);
            List<NoticeBoard> noticeBoards = List.of(NO_FK_NOTICE_BOARD);
            Mockito.when(noticeBoardService.getNoneFixedNoticeBoards(any(NoticeBoardSearchParam.class), any(PageRequest.class)))
                    .thenReturn(
                            new PageResponse<>(
                                    noticeBoards,
                                    pageRequest,
                                    totalCount));

            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_NONE_FIXED_NOTICE_BOARDS_URI)
                            .params(params))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect((result -> assertThat(result
                            .getResolvedException())
                            .isInstanceOf(MethodArgumentNotValidException.class)));
        }

        public static Stream<Arguments> specialWordSortParams() {
            return Stream.of(
                    Arguments.of("aaa", "a;b"),
                    Arguments.of("dsaf;", "abc"),
                    Arguments.of("dsa//z,,.", "abc"),
                    Arguments.of("dsaf;", "fdsa;;,./.,/"));
        }
    }

    @Nested
    @DisplayName("게시글 아이디로 공지 게시글 조회 컨트롤러 테스트")
    public class GetNoticeBoard {
        private static final String GET_NOTICE_BOARD_URI = "/api/boards/notice/%s";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long noticeBoardId = 13L;
            NoticeBoard noticeBoard = NoticeBoard.builder()
                    .noticeBoardId(noticeBoardId)
                    .category("category")
                    .title("title")
                    .content("content")
                    .createdAt(LocalDateTime.now())
                    .isFixed(false)
                    .memberId(1563L)
                    .build();
            //when
            Mockito.when(noticeBoardService.getNoticeBoardById(noticeBoardId))
                    .thenReturn(noticeBoard);
            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_NOTICE_BOARD_URI.formatted(noticeBoardId)))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.noticeBoardId").value(noticeBoard.getNoticeBoardId()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.category").value(noticeBoard.getCategory()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.title").value(noticeBoard.getTitle()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.createdAt").value(noticeBoard.getCreatedAt().toString()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.isFixed").value(noticeBoard.getIsFixed()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.memberId").value(noticeBoard.getMemberId()))
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.memberName").value(noticeBoard.getMemberName()))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("게시글 아이디로 공지 게시글 삭제 컨트롤러 테스트")
    public class DeleteNoticeBoardById {
        private static final String DELETE_NOTICE_BOARD_URI = "/admin/boards/notice/%s";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long noticeBoardId = 153L;
            Long memberId = 15153L;
            //when

            //then
            mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_NOTICE_BOARD_URI.formatted(noticeBoardId))
                            .sessionAttr(ADMIN_SESSION_KEY, Member.builder()
                                    .memberId(memberId)
                                    .build()))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @DisplayName("관리자 로그인이 되어있지 않을때")
        @Test
        public void notAuthenticated() throws Exception{
            //given
            Long noticeBoardId = 153L;
            //when

            //then
            mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_NOTICE_BOARD_URI.formatted(noticeBoardId)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("게시글 아이디로 공지 게시글 수정 컨트롤러 테스트")
    public class UpdateNoticeBoardTest {

        private static final String NOTICE_BOARD_UPDATE_URI = "/admin/boards/notice/%s";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long noticeBoardId = 1535L;
            NoticeBoard noticeBoard = NoticeBoard.builder()
                    .category("category1213")
                    .title("title34322")
                    .content("content15341")
                    .isFixed(true)
                    .build();

            Member member = Member.builder()
                    .memberId(1534L)
                    .build();
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.put(NOTICE_BOARD_UPDATE_URI.formatted(noticeBoardId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(noticeBoard))
                            .sessionAttr(ADMIN_SESSION_KEY, member))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @DisplayName("관리자 로그인이 안되어있을때")
        @Test
        public void noAuthentication() throws Exception{
            //given
            long noticeBoardId = 1535L;
            NoticeBoard noticeBoard = NoticeBoard.builder()
                    .build();

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.put(NOTICE_BOARD_UPDATE_URI.formatted(noticeBoardId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(noticeBoard)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @DisplayName("요청 인자 테스트")
        @ParameterizedTest
        @MethodSource("invalidInputs")
        public void test(String category, String title, String content, Boolean isFixed) throws Exception{
            //given
            int noticeBoardId = 1531;
            NoticeBoard board = NoticeBoard.builder()
                    .category(category)
                    .title(title)
                    .content(content)
                    .isFixed(isFixed)
                    .build();

            Member member = Member.builder()
                    .memberId(1534L)
                    .build();
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.put(NOTICE_BOARD_UPDATE_URI.formatted(noticeBoardId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(board))
                            .sessionAttr(ADMIN_SESSION_KEY, member))

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
}