package com.example.notice.service;

import com.example.notice.dto.FreeBoardSearchParam;
import com.example.notice.entity.FreeBoard;
import com.example.notice.exception.BoardNotExistException;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.mock.repository.MockFreeBoardRepository;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.example.notice.mock.repository.MockFreeBoardRepository.SAVED_FREE_BOARD;


class FreeBoardServiceTest {

    FreeBoardService freeBoardService = new FreeBoardServiceImpl(new MockFreeBoardRepository());

    @Nested
    @DisplayName("게시글 생성 테스트")
    public class createBoardTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            FreeBoard board = FreeBoard.builder()
                    .title("title")
                    .category("category")
                    .content("content")
                    .build();
            //when
            //then
            freeBoardService.createFreeBoard(board);
        }
    }

    @Nested
    @DisplayName("게시글 식별자로 게시글 가져오기 테스트")
    public class getFreeBoardTest {

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            //when
            FreeBoard findFreeBoard = freeBoardService.getBoardById(SAVED_FREE_BOARD.getFreeBoardId());
            //then
            Assertions.assertThat(SAVED_FREE_BOARD.getViews()+1).isEqualTo(findFreeBoard.getViews());
        }

        @DisplayName("식별자에 해당하는 게시글이 없을때")
        @Test
        public void notExistBoard() throws Exception{
            //given
            //when
            //then
            Assertions
                    .assertThatThrownBy(() -> freeBoardService.getBoardById(0L))
                    .isInstanceOf(BoardNotExistException.class);
        }
    }

    @Nested
    @DisplayName("게시글 검색 서비스 테스트")
    public class FreeBoardSearch {
        //TODO 이런 테스트에서 어떤걸 해야할지 모르겠다. -> repo에 매우 의존적인 service(조회/삽입 정도가 끝)
        // -> repo는 mocking 되어있어 내가 mocking하는 코드를 또 테스트하는 이중테스트 느낌이 난다.
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            FreeBoardSearchParam freeBoardSearchParam = new FreeBoardSearchParam(
                    LocalDateTime.now().minusMonths(2L),
                    LocalDateTime.now(),
                    "category",
                    "keyWord");
            PageRequest pageRequest = new PageRequest(5, 0, null, null);
            //when
            //then
            PageResponse<FreeBoard> result = freeBoardService.getBoardsBySearchParams(freeBoardSearchParam, pageRequest);
            Assertions.assertThat(result.getContent().size()).isEqualTo(result.getTotalCount());
            Assertions.assertThat(result.getPageSize()).isEqualTo(pageRequest.getSize());
            Assertions.assertThat(result.getCurrentPage()).isEqualTo(pageRequest.getCurrentPage());
        }
    }
}