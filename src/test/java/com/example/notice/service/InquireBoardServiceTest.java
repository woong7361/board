package com.example.notice.service;

import com.example.notice.dto.request.InquireBoardSearchDTO;
import com.example.notice.dto.response.InquireBoardResponseDTO;
import com.example.notice.dto.response.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireAnswer;
import com.example.notice.entity.InquireBoard;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.repository.InquireAnswerRepository;
import com.example.notice.repository.InquireBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.notice.mock.dummy.TestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class InquireBoardServiceTest {

    private InquireAnswerRepository inquireAnswerRepository = Mockito.mock(InquireAnswerRepository.class);
    private InquireBoardRepository inquireBoardRepository = Mockito.mock(InquireBoardRepository.class);
    private InquireBoardService inquireBoardService = new InquireBoardServiceImpl(
            inquireBoardRepository,
            inquireAnswerRepository);

    @Nested
    @DisplayName("문의게시판 생성 테스트")
    public class InquireBoardCreateTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            InquireBoard secretRequest = getInquireBoardCreateRequest(true);
            InquireBoard noneSecretRequest = getInquireBoardCreateRequest(false);
            Long memberId = 68464L;

            //when
            //then
            inquireBoardService.createBoard(secretRequest, memberId);
            inquireBoardService.createBoard(noneSecretRequest, memberId);
        }
    }

    @Nested
    @DisplayName("문의 게시판 게시글 검색 테스트")
    public class InquireBoardSearchTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long memberId = 53145L;
            int totalCount = 4648641;

            InquireBoard inquireBoard1 = getSavedInquireBoard(11L);
            InquireBoard inquireBoard2 = getSavedInquireBoard(12L);
            List<InquireBoardSearchResponseDTO> searchResult = List.of(
                    new InquireBoardSearchResponseDTO(inquireBoard1, false),
                    new InquireBoardSearchResponseDTO(inquireBoard2, true));

            InquireBoardSearchDTO searchParam =
                    new InquireBoardSearchDTO(LocalDateTime.now().minusYears(1L), LocalDateTime.now(), "key", memberId);
            PageRequest pageRequest = new PageRequest(10, 2, "1", "2");

            //when
            Mockito.when(inquireBoardRepository.getSearchTotalCount(searchParam))
                    .thenReturn(totalCount);
            Mockito.when(inquireBoardRepository.search(searchParam, pageRequest))
                    .thenReturn(searchResult);

            PageResponse<InquireBoardSearchResponseDTO> result =
                    inquireBoardService.searchInquireBoard(searchParam, pageRequest);

            //then
            Assertions.assertThat(result.getTotalCount()).isEqualTo(totalCount);
            Assertions.assertThat(result.getContentSize()).isEqualTo(searchResult.size());
            Assertions.assertThat(result.getContents().get(0))
                    .usingRecursiveAssertion()
                    .isEqualTo(searchResult.get(0));
        }

        @DisplayName("검색 결과가 없을때")
        @Test
        public void emptyRepository() throws Exception{
            //given
            Long memberId = 53145L;
            int totalCount = 0;

            List<InquireBoardSearchResponseDTO> searchResult = List.of();

            InquireBoardSearchDTO searchParam =
                    new InquireBoardSearchDTO(LocalDateTime.now().minusYears(1L), LocalDateTime.now(), "key", memberId);
            PageRequest pageRequest = new PageRequest(10, 2, "1", "2");

            //when
            Mockito.when(inquireBoardRepository.getSearchTotalCount(searchParam))
                    .thenReturn(totalCount);
            Mockito.when(inquireBoardRepository.search(searchParam, pageRequest))
                    .thenReturn(searchResult);

            PageResponse<InquireBoardSearchResponseDTO> result =
                    inquireBoardService.searchInquireBoard(searchParam, pageRequest);

            //then
            Assertions.assertThat(result.getTotalCount()).isEqualTo(totalCount);
            Assertions.assertThat(result.getContentSize()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("문의 게시판 게시글 조회 테스트")
    public class InquireBoardGetTest {
        @DisplayName("공개 문의글 조회")
        @Test
        public void getOpenBoard() throws Exception {
            //given
            long inquireBoardId = 854641L;

            InquireBoard inquireBoard = getSavedInquireBoard(inquireBoardId, false);
            List<InquireAnswer> answers = List.of(getSavedInquireAnswer(inquireBoard.getInquireBoardId()));

            InquireBoardResponseDTO response = InquireBoardResponseDTO.builder()
                    .inquireBoard(inquireBoard)
                    .inquireAnswers(answers)
                    .build();

            //when
            Mockito.when(inquireBoardRepository.findById(inquireBoard.getInquireBoardId()))
                    .thenReturn(Optional.of(response));

            //then
            inquireBoardService.getBoardById(inquireBoardId, inquireBoard.getMemberId());
        }

        @DisplayName("비공개 문의글 조회")
        @Test
        public void getSecretBoard() throws Exception {
            //given
            long inquireBoardId = 854641L;

            InquireBoard inquireBoard = getSavedInquireBoard(inquireBoardId, true);
            List<InquireAnswer> answers = List.of(getSavedInquireAnswer(inquireBoard.getInquireBoardId()));

            InquireBoardResponseDTO response = InquireBoardResponseDTO.builder()
                    .inquireBoard(inquireBoard)
                    .inquireAnswers(answers)
                    .build();

            //when
            Mockito.when(inquireBoardRepository.findById(inquireBoard.getInquireBoardId()))
                    .thenReturn(Optional.of(response));

            //then
            inquireBoardService.getBoardById(inquireBoardId, inquireBoard.getMemberId());
        }

        @DisplayName("비공개 문의글 조회 권한 없음")
        @Test
        public void getSecretBoardNotAuthorized() throws Exception {
            //given
            long inquireBoardId = 854641L;
            long invalidMemberId = -1L;

            InquireBoard inquireBoard = getSavedInquireBoard(inquireBoardId, true);
            List<InquireAnswer> answers = List.of(getSavedInquireAnswer(inquireBoard.getInquireBoardId()));

            InquireBoardResponseDTO response = InquireBoardResponseDTO.builder()
                    .inquireBoard(inquireBoard)
                    .inquireAnswers(answers)
                    .build();

            //when
            Mockito.when(inquireBoardRepository.findById(inquireBoard.getInquireBoardId()))
                    .thenReturn(Optional.of(response));

            //then
            Assertions.assertThatThrownBy(() -> inquireBoardService.getBoardById(inquireBoardId, invalidMemberId))
                    .isInstanceOf(AuthorizationException.class);
        }

        @DisplayName("식별자에 해당하는 게시글이 존재하지 않을때")
        @Test
        public void notExistBoard() throws Exception{
            //given
            Long inquireBoardId = 4531L;
            Long memberId = 54321L;

            //when
            Mockito.when(inquireBoardRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            //then
            Assertions.assertThatThrownBy(() -> inquireBoardService.getBoardById(inquireBoardId, memberId))
                    .isInstanceOf(EntityNotExistException.class);
        }
    }

    @Nested
    @DisplayName("알림 게시판 게시글 수정 테스트")
    public class InquireBoardUpdateTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            InquireBoard inquireBoard = getSavedInquireBoard();

            //when
            Mockito.when(inquireBoardRepository
                            .findByInquireBoardIdAndMemberId(inquireBoard.getInquireBoardId(), inquireBoard.getMemberId()))
                    .thenReturn(Optional.of(inquireBoard));

            //then
            inquireBoardService.updateById(inquireBoard, inquireBoard.getInquireBoardId(), inquireBoard.getMemberId());
        }
    }

    @Nested
    @DisplayName("문의 게시판 게시글 삭제 테스트")
    public class InquireBoardDeleteTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long memberId = 3453413L;
            Long inquireBoardId = 453154L;

            //when
            Mockito.when(inquireBoardRepository.findByInquireBoardIdAndMemberId(inquireBoardId, memberId))
                    .thenReturn(Optional.of(InquireBoard.builder().build()));

            //then
            inquireBoardService.deleteById(inquireBoardId, memberId);
        }

        @DisplayName("게시글 삭제 권한이 부족할때")
        @Test
        public void notOwner() throws Exception{
            //given
            Long memberId = 3453413L;
            Long inquireBoardId = 453154L;

            //when
            Mockito.when(inquireBoardRepository.findByInquireBoardIdAndMemberId(inquireBoardId, memberId))
                    .thenReturn(Optional.empty());

            //then
            Assertions.assertThatThrownBy(() -> inquireBoardService.deleteById(inquireBoardId, memberId))
                    .isInstanceOf(AuthorizationException.class);
        }
    }

    @Nested
    @DisplayName("관리자의 문의 게시글 삭제")
    public class DeleteInquireBoardByAdmin {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long inquireBoardId = 6452314L;

            //when
            //then
            inquireBoardService.deleteByAdmin(inquireBoardId);
        }
    }

    @Nested
    @DisplayName("관리자의 문의 게시글 조회")
    public class getInquireBoardByAdmin {
        @DisplayName("비밀글이 아닐때")
        @Test
        public void noneSecretInquireBoard() throws Exception {
            //given
            Long inquireBoardId = 984514L;
            InquireBoard inquireBoard = getSavedInquireBoard(inquireBoardId, false);
            InquireAnswer answer = getSavedInquireAnswer(inquireBoardId);

            //when
            Mockito.when(inquireBoardRepository.findById(inquireBoardId))
                    .thenReturn(Optional.of(new InquireBoardResponseDTO(null, inquireBoard, List.of(answer))));

            InquireBoardResponseDTO result = inquireBoardService.getBoardByAdmin(inquireBoardId);

            //then
            Assertions.assertThat(result.getInquireBoard()).usingRecursiveComparison()
                    .isEqualTo(inquireBoard);
            Assertions.assertThat(result.getInquireAnswers().get(0)).usingRecursiveComparison()
                    .isEqualTo(answer);
        }

        @DisplayName("비밀글일때")
        @Test
        public void SecretInquireBoard() throws Exception {
            //given
            Long inquireBoardId = 984514L;
            InquireBoard inquireBoard = getSavedInquireBoard(inquireBoardId, true);
            InquireAnswer answer = getSavedInquireAnswer(inquireBoardId);

            //when
            Mockito.when(inquireBoardRepository.findById(inquireBoardId))
                    .thenReturn(Optional.of(new InquireBoardResponseDTO(null, inquireBoard, List.of(answer))));

            InquireBoardResponseDTO result = inquireBoardService.getBoardByAdmin(inquireBoardId);

            //then
            Assertions.assertThat(result.getInquireBoard()).usingRecursiveComparison()
                    .isEqualTo(inquireBoard);
            Assertions.assertThat(result.getInquireAnswers().get(0)).usingRecursiveComparison()
                    .isEqualTo(answer);
        }
    }
}