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

            Assertions.assertThat(result.getContent().size()).isEqualTo(result.getTotalCount());
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
            Member member = Member.builder()
                    .memberId(888L)
                    .build();
            FreeBoard board = FreeBoard.builder()
                    .freeBoardId(999L)
                    .member(member)
                    .build();
            freeBoardRepository.save(board);


            //when
            freeBoardService.deleteFreeBoard(board.getFreeBoardId(), member);

            //then
            Assertions.assertThat(freeBoardRepository.findBoardById(board.getFreeBoardId()))
                    .isEqualTo(Optional.empty());
        }

        @DisplayName("owner가 아닌 사용자가 삭제를 요청할때")
        @Test
        public void notBoardOwner() throws Exception{
            //given
            Member member = Member.builder()
                    .memberId(888L)
                    .build();
            FreeBoard board = FreeBoard.builder()
                    .freeBoardId(999L)
                    .member(member)
                    .build();

            Member notOwnerMember = Member.builder()
                    .memberId(10L)
                    .build();

            freeBoardRepository.save(board);

            //when
            //then
            Assertions.assertThatThrownBy(() -> freeBoardService.deleteFreeBoard(board.getFreeBoardId(), notOwnerMember))
                    .isInstanceOf(AuthorizationException.class);

            //last process
            freeBoardService.deleteFreeBoard(board.getFreeBoardId(),member);
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
}