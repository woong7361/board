package com.example.notice.service;

import com.example.notice.entity.FreeBoard;
import com.example.notice.exception.BoardNotExistException;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.mock.repository.MockFreeBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
            Assertions.assertThat(SAVED_FREE_BOARD).usingRecursiveComparison().isEqualTo(findFreeBoard);
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
}