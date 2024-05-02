package com.example.notice.service;

import com.example.notice.dto.request.FreeBoardSearchDTO;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.entity.FreeBoard;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.exception.BoardNotExistException;
import com.example.notice.files.PhysicalFileRepository;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.repository.AttachmentFileRepository;
import com.example.notice.repository.FreeBoardRepository;
import com.example.notice.utils.FileUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.notice.mock.dummy.TestData.getSavedFreeBoard;
import static org.mockito.ArgumentMatchers.any;


class FreeBoardServiceTest {
    FreeBoardRepository freeBoardRepository = Mockito.mock(FreeBoardRepository.class);
    FreeBoardService freeBoardService = new FreeBoardServiceImpl(
            freeBoardRepository
    );

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

            long memberId = 154641L;

            //when
            freeBoardService.createFreeBoard(board, memberId);
            freeBoardService.createFreeBoard(board, memberId);
            freeBoardService.createFreeBoard(board, memberId);
            //then
        }
    }

    @Nested
    @DisplayName("게시글 식별자로 게시글 가져오기 테스트")
    public class getFreeBoardTest {

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            FreeBoard savedFreeBoard = getSavedFreeBoard();

            //when
            Mockito.when(freeBoardRepository.findBoardById(savedFreeBoard.getFreeBoardId()))
                    .thenReturn(Optional.of(savedFreeBoard));

            FreeBoard findFreeBoard = freeBoardService.getBoardById(savedFreeBoard.getFreeBoardId());

            //then
            Assertions.assertThat(findFreeBoard).usingRecursiveAssertion()
                    .isEqualTo(savedFreeBoard);
        }

        @DisplayName("식별자에 해당하는 게시글이 없을때")
        @Test
        public void notExistBoard() throws Exception{
            //given
            long freeBoardId = 45641L;

            //when
            Mockito.when(freeBoardRepository.findBoardById(any()))
                    .thenReturn(Optional.empty());

            //then
            Assertions
                    .assertThatThrownBy(() -> freeBoardService.getBoardById(freeBoardId))
                    .isInstanceOf(BoardNotExistException.class);
        }
    }

    @Nested
    @DisplayName("게시글 검색 서비스 테스트")
    public class FreeBoardSearchTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            FreeBoardSearchDTO freeBoardSearchDTO = new FreeBoardSearchDTO(
                    LocalDateTime.now().minusMonths(2L),
                    LocalDateTime.now(),
                    "category",
                    "keyWord");

            PageRequest pageRequest = new PageRequest(5, 0, "1", "2");

            int totalCount = 2;

            //when
            Mockito.when(freeBoardRepository.getTotalCountBySearchParam(freeBoardSearchDTO))
                    .thenReturn(totalCount);

            Mockito.when(freeBoardRepository.findBoardsBySearchParam(freeBoardSearchDTO, pageRequest))
                    .thenReturn(List.of(getSavedFreeBoard(1L), getSavedFreeBoard(2L)));

            //then
            PageResponse<FreeBoard> result = freeBoardService.getBoardsBySearchParams(freeBoardSearchDTO, pageRequest);

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
            Long freeBoardId = 15631L;
            Long memberId = 7864L;

            //when
            Mockito.when(freeBoardRepository.findBoardByIdAndMemberId(freeBoardId, memberId))
                    .thenReturn(Optional.of(FreeBoard.builder().build()));

            Mockito.when(freeBoardRepository.hasCommentByBoardId(freeBoardId))
                    .thenReturn(false);

            //then
            freeBoardService.deleteFreeBoardById(freeBoardId, memberId);
        }

        @DisplayName("게시글에 코멘트가 달려있을때")
        @Test
        public void boardHasComment() throws Exception{
            //given
            Long freeBoardId = 15631L;
            Long memberId = 7864L;

            //when
            Mockito.when(freeBoardRepository.findBoardByIdAndMemberId(freeBoardId, memberId))
                    .thenReturn(Optional.of(FreeBoard.builder().build()));

            Mockito.when(freeBoardRepository.hasCommentByBoardId(freeBoardId))
                    .thenReturn(true);

            //then
            freeBoardService.deleteFreeBoardById(freeBoardId, memberId);
        }

        @DisplayName("게시글 삭제 권한이 없을때")
        @Test
        public void notAuthorizationInDelete() throws Exception{
            //given
            Long freeBoardId = 15631L;
            Long memberId = 7864L;

            //when
            Mockito.when(freeBoardRepository.findBoardByIdAndMemberId(freeBoardId, memberId))
                    .thenReturn(Optional.empty());

            //then
            Assertions.assertThatThrownBy(() -> freeBoardService.deleteFreeBoardById(freeBoardId, memberId))
                    .isInstanceOf(AuthorizationException.class);
        }
    }

    @Nested
    @DisplayName("게시글 수정 서비스 테스트")
    public class FreeBoardUpdateTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long freeBoardId = 15631L;
            Long memberId = 7864L;

            FreeBoard freeBoard = getSavedFreeBoard();

            //when
            Mockito.when(freeBoardRepository.findBoardByIdAndMemberId(freeBoardId, memberId))
                    .thenReturn(Optional.of(FreeBoard.builder().build()));

            //then
            freeBoardService.updateFreeBoardById(freeBoard, freeBoardId, memberId);
        }

        @DisplayName("게시글 수정 권한이 없을때")
        @Test
        public void notAuthorizationInEdit() throws Exception {
            //given
            Long freeBoardId = 15631L;
            Long memberId = 7864L;

            FreeBoard freeBoard = getSavedFreeBoard();

            //when
            Mockito.when(freeBoardRepository.findBoardByIdAndMemberId(freeBoardId, memberId))
                    .thenReturn(Optional.empty());

            //then
            Assertions.assertThatThrownBy(() -> freeBoardService.updateFreeBoardById(freeBoard, freeBoardId, memberId));
        }
    }


    @Nested
    @DisplayName("관리자의 게시글 삭제")
    public class DeleteFreeBoardByAdmin {
        @DisplayName("정상처리")
        @Test
        public void success() throws Exception {
            //given
            long memberId = 8763L;
            long freeBoardId = 4635L;

            //when
            //then
            freeBoardService.deleteFreeBoardByAdmin(freeBoardId, memberId);
        }
    }

    @Nested
    @DisplayName("자유게시판 카테고리 가져오기")
    public class GetFreeBoardCategory {
        @DisplayName("정상처리")
        @Test
        public void success() throws Exception {
            //given
            //when
            //then
            freeBoardService.getCategories();
        }
    }

}