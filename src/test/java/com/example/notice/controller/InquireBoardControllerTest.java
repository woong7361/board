package com.example.notice.controller;

import com.example.notice.constant.ResponseConstant;
import com.example.notice.dto.response.InquireBoardResponseDTO;
import com.example.notice.dto.response.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireAnswer;
import com.example.notice.entity.InquireBoard;
import com.example.notice.mock.util.LoginTestUtil;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.restdocs.RestDocsHelper;
import com.example.notice.service.InquireBoardService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.cookies.CookieDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InquireBoardController.class)
class InquireBoardControllerTest extends RestDocsHelper {

    @MockBean
    InquireBoardService inquireBoardService;

    @BeforeEach
    public void initMapper() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Nested
    @DisplayName("문의 게시판 게시글 생성 컨트롤러 테스트")
    public class InquireBoardCreateControllerTest {
        private static final String INQUIRE_BOARD_CREATE_URI = "/api/boards/inquire";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long memberId = 4561L;

            InquireBoard inquireBoard = InquireBoard.builder()
                    .title("title")
                    .content("content")
                    .isSecret(true)
                    .build();

            //when
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(INQUIRE_BOARD_CREATE_URI)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(memberId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(inquireBoard)));

            //then
            action
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$." + ResponseConstant.INQUIRE_BOARD_ID_PARAM)
                            .exists())


                    .andDo(restDocs.document(
                            PayloadDocumentation.requestFields(
                                    PayloadDocumentation.fieldWithPath("title")
                                            .description("제목"),
                                    PayloadDocumentation.fieldWithPath("content")
                                            .description("내용"),
                                    PayloadDocumentation.fieldWithPath("isSecret")
                                            .description("비밀글 여부"))))
                    .andDo(restDocs.document(
                            responseFields(
                                    fieldWithPath("inquireBoardId")
                                            .description("게시글 식별자"))))
                    .andDo(restDocs.document(
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT token")
                            )
                    ));;
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
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(INQUIRE_BOARD_CREATE_URI)
                    .headers(authenticationTestUtil.getLoginTokenHeaders())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(inquireBoard)));

            //then
            action
                    .andExpect(status().isBadRequest());
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

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("size", "5");
            params.add("currentPage", "0");
            params.add("keyWord", "4153");
            params.add("onlyMine", "false");
            params.add("orderColumn", "created_at");
            params.add("orderType", "modified_at");

            PageRequest pageRequest = new PageRequest(5, 0, null, null);

            InquireBoardSearchResponseDTO b1 = InquireBoardSearchResponseDTO.builder()
                    .inquireBoardId(1231L)
                    .title("board title1")
                    .content("board content1")
                    .isSecret(false)
                    .views(4153L)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .memberId(5341456L)
                    .memberName("member name1")
                    .isAnswered(true)
                    .build();

            InquireBoardSearchResponseDTO b2 = InquireBoardSearchResponseDTO.builder()
                    .inquireBoardId(123112L)
                    .title("board title2")
                    .content("board content2")
                    .isSecret(true)
                    .views(4155633L)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .memberId(53414561L)
                    .memberName("member name2")
                    .isAnswered(false)
                    .build();

            PageResponse<InquireBoardSearchResponseDTO> result =
                    new PageResponse<>(List.of(b1, b2), pageRequest, 2);

            //when
            Mockito.when(inquireBoardService.searchInquireBoard(any(), any()))
                    .thenReturn(result);

            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(INQUIRE_BOARD_SEARCH_URI)
                    .params(params));

            //then
            action
                    .andExpect(status().isOk())


                    //rest docs
                    .andDo(restDocs.document(
                            queryParameters(
                                    parameterWithName("size").description("가져올 개수"),
                                    parameterWithName("currentPage").description("현재 페이지"),
                                    parameterWithName("keyWord").description("검색 키워드"),
                                    parameterWithName("onlyMine").description("자신의 것만 검색할지"),
                                    parameterWithName("orderColumn").description("정렬 인자"),
                                    parameterWithName("orderType").description("정렬 조건")
                            )))
                    .andDo(restDocs.document(
                            responseFields(
                                    fieldWithPath("pageOffset").description("offset"),
                                    fieldWithPath("currentPage").description("현재 페이지"),
                                    fieldWithPath("totalCount").description("전체 개수"),
                                    fieldWithPath("contentSize").description("반환한 content 수"),
                                    fieldWithPath("pageSize").description("페이지 크기"),
                                    fieldWithPath("contents[].inquireBoardId").description("페이지 크기"),
                                    fieldWithPath("contents[].memberId").description("회원 식별자"),
                                    fieldWithPath("contents[].memberName").description("회원 이름"),
                                    fieldWithPath("contents[].createdAt").description("생성 일자"),
                                    fieldWithPath("contents[].modifiedAt").description("수정 일자"),
                                    fieldWithPath("contents[].title").description("제목"),
                                    fieldWithPath("contents[].content").description("내용"),
                                    fieldWithPath("contents[].isSecret").description("비밀글 여부"),
                                    fieldWithPath("contents[].views").description("조회수"),
                                    fieldWithPath("contents[].isAnswered").description("응답 여부")
                            )
                    ));

        }
    }


    @Nested
    @DisplayName("문의 게시판 게시글 조회 컨트롤러 테스트")
    public class InquireBoardGetControllerTest {
        private static final String GET_BOARD_CONTROLLER_URI = "/api/boards/inquire/{inquireBoardId}";
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
                    .memberName("member name")
                    .build();

            InquireAnswer answer = InquireAnswer.builder()
                    .inquireAnswerId(54135L)
                    .inquireBoardId(findBoard.getInquireBoardId())
                    .answer("inquire answer")
                    .memberId(354114L)
                    .memberName("member name")
                    .build();

            InquireBoardResponseDTO board = InquireBoardResponseDTO.builder()
                    .inquireBoard(findBoard)
                    .inquireAnswers(List.of(answer))
                    .build();
            //when
            Mockito.when(inquireBoardService.getBoardById(any(), any()))
                    .thenReturn(board);

            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders
                    .get(GET_BOARD_CONTROLLER_URI, boardId)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(485641534L)));

            //then
            action
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.inquireBoard.inquireBoardId").value(boardId))
                    .andExpect(jsonPath("$.inquireBoard.content").value(board.getInquireBoard().getContent()))
                    .andExpect(jsonPath("$.inquireBoard.title").value(board.getInquireBoard().getTitle()))
                    .andExpect(jsonPath("$.inquireBoard.views").value(board.getInquireBoard().getViews()))
                    .andExpect(jsonPath("$.inquireBoard.memberId").value(board.getInquireBoard().getMemberId()))

                    //rest docs
                    .andDo(restDocs.document(
                            pathParameters(
                                    parameterWithName("inquireBoardId")
                                            .description("문의 게시글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(
                            responseFields(
                                    fieldWithPath("inquireBoard.inquireBoardId").description("게시글 식별자"),
                                    fieldWithPath("inquireBoard.createdAt").description("생성 일자"),
                                    fieldWithPath("inquireBoard.modifiedAt").description("수정 일자"),
                                    fieldWithPath("inquireBoard.title").description("제목"),
                                    fieldWithPath("inquireBoard.content").description("내용"),
                                    fieldWithPath("inquireBoard.isSecret").description("비밀글 여부"),
                                    fieldWithPath("inquireBoard.views").description("조회수"),
                                    fieldWithPath("inquireBoard.memberName").description("작성자 이름"),
                                    fieldWithPath("inquireBoard.memberId").description("작성자 식별자"),
                                    fieldWithPath("inquireAnswers[].inquireAnswerId").description("답변 식별자"),
                                    fieldWithPath("inquireAnswers[].inquireBoardId").description("게시글 식별자"),
                                    fieldWithPath("inquireAnswers[].answer").description("답변 내용"),
                                    fieldWithPath("inquireAnswers[].memberName").description("답변 작성자 이름"),
                                    fieldWithPath("inquireAnswers[].memberId").description("답변 작성자 식별자")
                            )
                    ))
                    .andDo(restDocs.document(
                            requestHeaders(
                                    headerWithName("Authorization")
                                            .description("JWT token")
                                            .optional()
                            )
                    ));


        }
    }

    @Nested
    @DisplayName("문의 게시판 게시글 수정 컨트롤러 테스트")
    public class InquireBoardUpdateControllerTest {

        private static final String UPDATE_INQUIRE_BOARD_URI = "/api/boards/inquire/{inquireBoardId}";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long boardId = 1546743L;
            InquireBoard inputBoard = InquireBoard.builder()
                    .title("title")
                    .content("board content")
                    .isSecret(false)
                    .build();

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders.put(UPDATE_INQUIRE_BOARD_URI, boardId)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(21564L))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(inputBoard)));

            //then
            action
                    .andExpect(status().isOk())

                    .andDo(restDocs.document(
                            pathParameters(
                                    parameterWithName("inquireBoardId")
                                            .description("문의 게시글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(
                            requestFields(
                                    fieldWithPath("title").description("제목"),
                                    fieldWithPath("content").description("내용"),
                                    fieldWithPath("isSecret").description("비밀글 여부")
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
            mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_INQUIRE_BOARD_URI, boardId)
                            .headers(authenticationTestUtil.getLoginTokenHeaders(455341L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(inquireBoard)))
                    .andExpect(status().isBadRequest());
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

        private static final String INQUIRE_BOARD_DELETE_URI = "/api/boards/inquire/{inquireBoardId}";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long boardId = 41564L;

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders
                    .delete(INQUIRE_BOARD_DELETE_URI, boardId)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(54341L)));

            //then

            action
                    .andExpect(status().isOk())

            //rest docs
                    .andDo(restDocs.document(
                            pathParameters(
                                    parameterWithName("inquireBoardId").description("게시글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT token")
                            )
                    ));

        }
    }

    @Nested
    @DisplayName("관리자의 문의 게시판 게시글 삭제")
    public class DeleteInquireBoardByAdminControllerTest {
        private static final String DELETE_INQUIRE_BOARD_BY_ADMIN_URL = "/admin/boards/inquire/{inquireBoardId}";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long boardId = 41564L;

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders
                    .delete(DELETE_INQUIRE_BOARD_BY_ADMIN_URL, boardId)
                    .session(LoginTestUtil.getMockAdminSession())
                    .cookie(LoginTestUtil.getMockSessionCookie()));

            //then

            action
                    .andExpect(status().isOk())

                    //rest docs
                    .andDo(restDocs.document(
                            pathParameters(
                                    parameterWithName("inquireBoardId").description("게시글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(requestCookies(
                            CookieDocumentation.cookieWithName("JSESSIONID").description("세션 쿠키")
                    )));
        }
    }

    @Nested
    @DisplayName("관리자의 문의 게시글 조회")
    public class GetInquireBoardByAdminControllerTest {
        private static final String GET_INQUIRE_BOARD_BY_ADMIN_URL = "/admin/boards/inquire/{inquireBoardId}";
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
                    .memberName("member name")
                    .build();

            InquireAnswer answer = InquireAnswer.builder()
                    .inquireAnswerId(54135L)
                    .inquireBoardId(findBoard.getInquireBoardId())
                    .answer("inquire answer")
                    .memberId(354114L)
                    .memberName("member name")
                    .build();

            InquireBoardResponseDTO board = InquireBoardResponseDTO.builder()
                    .inquireBoard(findBoard)
                    .inquireAnswers(List.of(answer))
                    .build();
            //when
            Mockito.when(inquireBoardService.getBoardByAdmin(any()))
                    .thenReturn(board);

            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders
                    .get(GET_INQUIRE_BOARD_BY_ADMIN_URL, boardId)
                    .session(LoginTestUtil.getMockAdminSession())
                    .cookie(LoginTestUtil.getMockSessionCookie()));

            //then
            action
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.inquireBoard.inquireBoardId").value(boardId))
                    .andExpect(jsonPath("$.inquireBoard.content").value(board.getInquireBoard().getContent()))
                    .andExpect(jsonPath("$.inquireBoard.title").value(board.getInquireBoard().getTitle()))
                    .andExpect(jsonPath("$.inquireBoard.views").value(board.getInquireBoard().getViews()))
                    .andExpect(jsonPath("$.inquireBoard.isSecret").value(board.getInquireBoard().getIsSecret()))
                    .andExpect(jsonPath("$.inquireBoard.memberId").value(board.getInquireBoard().getMemberId()))
                    .andExpect(jsonPath("$.inquireBoard.memberName").value(board.getInquireBoard().getMemberName()))

                    //rest docs
                    .andDo(restDocs.document(
                            pathParameters(
                                    parameterWithName("inquireBoardId")
                                            .description("문의 게시글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(
                            responseFields(
                                    fieldWithPath("inquireBoard.inquireBoardId").description("게시글 식별자"),
                                    fieldWithPath("inquireBoard.createdAt").description("생성 일자"),
                                    fieldWithPath("inquireBoard.modifiedAt").description("수정 일자"),
                                    fieldWithPath("inquireBoard.title").description("제목"),
                                    fieldWithPath("inquireBoard.content").description("내용"),
                                    fieldWithPath("inquireBoard.isSecret").description("비밀글 여부"),
                                    fieldWithPath("inquireBoard.views").description("조회수"),
                                    fieldWithPath("inquireBoard.memberName").description("작성자 이름"),
                                    fieldWithPath("inquireBoard.memberId").description("작성자 식별자"),
                                    fieldWithPath("inquireAnswers[].inquireAnswerId").description("답변 식별자"),
                                    fieldWithPath("inquireAnswers[].inquireBoardId").description("게시글 식별자"),
                                    fieldWithPath("inquireAnswers[].answer").description("답변 내용"),
                                    fieldWithPath("inquireAnswers[].memberName").description("답변 작성자 이름"),
                                    fieldWithPath("inquireAnswers[].memberId").description("답변 작성자 식별자")
                            )
                    ))
                    .andDo(restDocs.document(requestCookies(
                            CookieDocumentation.cookieWithName("JSESSIONID").description("세션 쿠키")
                    )));
        }
    }
}