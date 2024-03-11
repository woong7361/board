package com.example.notice.controller;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.dto.FreeBoardCreateRequest;
import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.service.FreeBoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
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
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.example.notice.constant.ResponseConstant.FREE_BOARD_ID_PARAM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
                        .andExpect(result -> Assertions
                                .assertThat(result.getResolvedException())
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

}