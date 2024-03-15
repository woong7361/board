package com.example.notice.service;

import com.example.notice.dto.FreeBoardSearchParam;
import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.exception.BoardNotExistException;
import com.example.notice.mock.repository.MockFreeBoardRepository;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.repository.FreeBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.notice.mock.repository.MockFreeBoardRepository.SAVED_FREE_BOARD;


class FreeBoardServiceTest {

    FreeBoardRepository freeBoardRepository = new MockFreeBoardRepository();
    FreeBoardService freeBoardService = new FreeBoardServiceImpl(freeBoardRepository);

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
    public class FreeBoardSearchTest {
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

            Assertions.assertThat(result.getContents().size()).isEqualTo(result.getTotalCount());
            Assertions.assertThat(result.getPageSize()).isEqualTo(pageRequest.getSize());
            Assertions.assertThat(result.getCurrentPage()).isEqualTo(pageRequest.getCurrentPage());
        }
    }

    @Nested
    @DisplayName("게시글 삭제 서비스 테스트")
    public class FreeBoardDeleteTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            FreeBoard board = FreeBoard.builder()
                    .freeBoardId(999L)
                    .build();
            freeBoardRepository.save(board);


            //when
            freeBoardService.deleteFreeBoard(board.getFreeBoardId());

            //then
            Assertions.assertThat(freeBoardRepository.findBoardById(board.getFreeBoardId()))
                    .isEqualTo(Optional.empty());
        }

        @Disabled
        @DisplayName("게시글에 코멘트가 달려있을때")
        @Test
        public void boardHasComment() throws Exception{
            //given

            //when

            //then
        }
    }

    @Nested
    @DisplayName("게시글 수정 서비스 테스트")
    public class FreeBoardUpdateTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            FreeBoard oldBoard = FreeBoard.builder()
                    .freeBoardId(9999L)
                    .member(Member.builder()
                            .memberId(10L)
                            .build())
                    .category("category")
                    .title("title")
                    .content("content")
                    .build();

            FreeBoard newBoard = FreeBoard.builder()
                    .freeBoardId(9999L)
                    .category("new category")
                    .title("new title")
                    .content("new content")
                    .build();

            //when
            freeBoardRepository.save(oldBoard);
            freeBoardRepository.update(newBoard, oldBoard.getFreeBoardId());
            FreeBoard findBoard = freeBoardRepository.findBoardById(oldBoard.getFreeBoardId())
                    .get();

            //then

            Assertions.assertThat(findBoard.getCategory()).isEqualTo(newBoard.getCategory());
            Assertions.assertThat(findBoard.getTitle()).isEqualTo(newBoard.getTitle());
            Assertions.assertThat(findBoard.getContent()).isEqualTo(newBoard.getContent());

            //finally
            freeBoardRepository.deleteByBoardId(oldBoard.getFreeBoardId());

        }
    }

    @Nested
    @DisplayName("게시글 권한 인증 테스트")
    public class FreeBoardAuthorizationTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            //when
            //then
            freeBoardService.checkFreeBoardAuthorization(
                    SAVED_FREE_BOARD.getFreeBoardId(),
                    SAVED_FREE_BOARD.getMemberId());
        }

        @DisplayName("게시글의 작성자가 아닌 다른 사용자가 접근할때")
        @Test
        public void anotherUser() throws Exception{
            //given
            Long anotherMemberId = 1561653L;
            if (SAVED_FREE_BOARD.getMemberId().equals(anotherMemberId)) {
                throw new AssertionError("같은 사용자아이디를 사용하는 테스트가 아니다.");
            }
            //when
            //then
            Assertions.assertThatThrownBy(() -> freeBoardService.checkFreeBoardAuthorization(
                            SAVED_FREE_BOARD.getFreeBoardId(),
                            anotherMemberId))
                    .isInstanceOf(AuthorizationException.class);
        }
    }
}