package com.example.notice.controller;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.constant.ResponseConstant;
import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.service.FreeBoardService;
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
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.example.notice.constant.ResponseConstant.FREE_BOARD_ID_PARAM;
import static com.example.notice.mock.repository.MockFreeBoardRepository.SAVED_FREE_BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@WebMvcTest(FreeBoardController.class)
class FreeBoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FreeBoardService freeBoardService;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void createMember() {
        Member member = Member.builder()
                .memberId(1)
                .build();
        Principal<Member> principal = new MemberPrincipal(member);

        AuthenticationHolder.setPrincipal(principal);
    }

    @Nested
    @DisplayName("자유게시판 생성 테스트")
    public class FreeBoardCreateTest {

        public String FREE_BOARD_CREATE_URI = "/api/boards/free";


        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            FreeBoard board = FreeBoard.builder()
                    .title("title")
                    .content("content")
                    .category("AAA")
                    .build();

            long boardId = 10L;
            when(freeBoardService.createFreeBoard(any())).thenReturn(boardId);
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(FREE_BOARD_CREATE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(board)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$." + FREE_BOARD_ID_PARAM).value(boardId));

        }

        @Nested
        @DisplayName("게시글 인자 검증 테스트")
        public class NestedClass {

            @DisplayName("제목이 null 일때 실패")
            @ParameterizedTest
            @MethodSource("invalidFreeBoardParam")
            public void invalidCreateInput(String title, String content, String category) throws Exception {
                //given
                FreeBoard board = FreeBoard.builder()
                        .title(title)
                        .content(content)
                        .category(category)
                        .build();

                //when
                //then
                mockMvc.perform(MockMvcRequestBuilders.post(FREE_BOARD_CREATE_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(board)))
                        .andExpect(result -> assertThat(result.getResolvedException())
                                .isInstanceOf(MethodArgumentNotValidException.class))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

            }

            private static Stream<Arguments> invalidFreeBoardParam() {
                return Stream.of(
                        Arguments.of(null, "content", "category"), // null일때
                        Arguments.of("title", null, "category"),
                        Arguments.of("title", "content", null),
                        Arguments.of("", "content", "category"), // 빈 문자열일때
                        Arguments.of("title", "", "category"),
                        Arguments.of("title", "content", "")
//                        Arguments.of("100", "content", ""), // 문자열 max 길이보다 길때 100
//                        Arguments.of("title", "content", "") // 4000

                );
            }
        }


    }

    @Nested
    @DisplayName("자유게시판 게시글 조회 테스트")
    public class getFreeBoardTest {
        private final String GET_FREE_BOARD_URI = "/api/boards/free";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            String freeBoardId = String.valueOf(1L);
            //when
            Mockito.when(freeBoardService.getBoardById(Long.valueOf(freeBoardId)))
                    .thenReturn(SAVED_FREE_BOARD);
            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_FREE_BOARD_URI + "/" + freeBoardId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.freeBoardId").value(SAVED_FREE_BOARD.getFreeBoardId()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(SAVED_FREE_BOARD.getTitle()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(SAVED_FREE_BOARD.getContent()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(SAVED_FREE_BOARD.getCategory()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.memberName").value(SAVED_FREE_BOARD.getMemberName()));
        }
    }

    @Nested
    @DisplayName("자유게시판 게시글 검색 테스트")
    public class SearchTest {
        private static final String GET_BOARDS_URI = "/api/boards/free";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", LocalDateTime.now().minusMonths(2L).toString());
            params.add("endDate", LocalDateTime.now().toString());
            params.add("category", "category");
            params.add("keyword", "keyword");
            params.add("size", "5");
            params.add("currentPage", "0");

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_BOARDS_URI)
                            .params(params))
                    .andExpect(MockMvcResultMatchers.status().isOk());

        }

        @DisplayName("설정한 최대 기간(1year)을 넘어갈때")
        @Test
        public void exceedMaxDuration() throws Exception{
            //given
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", LocalDateTime.now().minusYears(2L).minusDays(1L).toString());
            params.add("endDate", LocalDateTime.now().toString());

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_BOARDS_URI)
                            .params(params))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect((result -> assertThat(result
                            .getResolvedException())
                            .isInstanceOf(BadRequestParamException.class)));
        }

        @DisplayName("정렬 인자에 특수문자가 들어갈때")
        @ParameterizedTest
        @MethodSource("specialWordSortParams")
        public void specialWordInSortParam(String orderColumn, String orderType) throws Exception{
            //given
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("orderColumn", orderColumn);
            params.add("orderType", orderType);

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_BOARDS_URI)
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
    @DisplayName("자유게시판 게시글 삭제 테스트")
    public class FreeBoardDeleteTest {

        private static final String BOARD_DELETE_URI = "/api/boards/free";
        @BeforeEach
        public void insertMember() {
            Member member = Member.builder()
                    .memberId(1L)
                    .name("name")
                    .build();

            AuthenticationHolder.setPrincipal(new MemberPrincipal(member));
        }

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            String boardId = "1";
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.delete(BOARD_DELETE_URI + "/" + boardId))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("자유게시판 게시글 수정 테스트")
    public class FreeBoardUpdateTest {

        @BeforeEach
        public void createMember() {
            Member member = Member.builder()
                    .memberId(153L)
                    .name("nane")
                    .build();

            AuthenticationHolder.setPrincipal(new MemberPrincipal(member));
        }
        private static final String FREE_BOARD_UPDATE_URI = "/api/boards/free";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long freeBoardId = 11L;
            FreeBoard board = FreeBoard.builder()
                    .freeBoardId(freeBoardId)
                    .category("category222")
                    .title("title222")
                    .content("content123")
                    .build();
            //when

            //then
            mockMvc.perform(MockMvcRequestBuilders.put(FREE_BOARD_UPDATE_URI + "/" + freeBoardId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(board)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.%s".formatted(FREE_BOARD_ID_PARAM)).value(freeBoardId));
        }

        @DisplayName("게시글 인자 테스트")
        @ParameterizedTest
        @MethodSource("invalidFreeBoardParam")
        public void invalidUpdateInput(String title, String content, String category) throws Exception {
            //given
            FreeBoard board = FreeBoard.builder()
                    .title(title)
                    .content(content)
                    .category(category)
                    .build();

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.put(FREE_BOARD_UPDATE_URI + "/" + 10L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(board)))
                    .andExpect(result -> assertThat(result.getResolvedException())
                            .isInstanceOf(MethodArgumentNotValidException.class))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

        }

        //TODO 반복되는 @Valid test 해결을 어떻게? -> spring docs같은 api문서를 test로 만들때도?
        private static Stream<Arguments> invalidFreeBoardParam() {
            return Stream.of(
                    Arguments.of(null, "content", "category"), // null일때
                    Arguments.of("title", null, "category"),
                    Arguments.of("title", "content", null),
                    Arguments.of("", "content", "category"), // 빈 문자열일때
                    Arguments.of("title", "", "category"),
                    Arguments.of("title", "content", "")
//                        Arguments.of("100", "content", ""), // 문자열 max 길이보다 길때 100
//                        Arguments.of("title", "content", "") // 4000
            );
        }
    }
}