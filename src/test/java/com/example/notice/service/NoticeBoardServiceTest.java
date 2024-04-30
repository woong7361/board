package com.example.notice.service;

import com.example.notice.config.ConfigurationService;
import com.example.notice.dto.request.NoticeBoardSearchDTO;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.repository.NoticeBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.notice.mock.dummy.TestData.getSavedNoticeBoard;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;


class NoticeBoardServiceTest {

    private NoticeBoardRepository noticeBoardRepository = Mockito.mock(NoticeBoardRepository.class);
    private ConfigurationService configurationService = Mockito.mock(ConfigurationService.class);
    private NoticeBoardService noticeBoardService = new NoticeBoardServiceImpl(
            noticeBoardRepository,
            configurationService);


    @Nested
    @DisplayName("알림 게시판 생성 테스트")
    public class CreateNoticeBoard {
        @DisplayName("정상 처리")
        @Test

        public void success() throws Exception {
            //given
            Long memberId = 453L;
            Long noticeBoardId = 4531L;
            NoticeBoard createRequest = NoticeBoard.builder()
                    .noticeBoardId(noticeBoardId)
                    .title("title")
                    .category("category")
                    .content("content")
                    .isFixed(true)
                    .build();

            //when
            Long createdId = noticeBoardService.createNoticeBoard(createRequest, memberId);

            //then
            Assertions.assertThat(createdId).isEqualTo(noticeBoardId);
        }
    }

    @Nested
    @DisplayName("고정된 공지 게시글 조회 테스트")
    public class GetFixedNoticeBoard {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            NoticeBoard fixedBoard1 = getSavedNoticeBoard(1L, 564121L, true);
            NoticeBoard fixedBoard2 = getSavedNoticeBoard(12L, 564121L, true);
            List<NoticeBoard> savedFixedBoards = List.of(fixedBoard1, fixedBoard2);

            //when
            Mockito.when(noticeBoardRepository.findFixedNoticeBoardByLimit(anyInt()))
                    .thenReturn(savedFixedBoards);

            List<NoticeBoard> findNoticeBoards = noticeBoardService.getFixedNoticeBoardWithoutContent();

            //then
            Assertions.assertThat(findNoticeBoards.size()).isEqualTo(savedFixedBoards.size());
            Assertions.assertThat(findNoticeBoards.get(0)).isEqualTo(fixedBoard1);

        }
    }

    @Nested
    @DisplayName("상단 고정 공지를 제외한 공지 검색 테스트")
    public class GetNoneFixedNoticeBoards {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            NoticeBoard noticeBoard1 = getSavedNoticeBoard(1L, 564121L, true);
            NoticeBoard noticeBoard2 = getSavedNoticeBoard(12L, 564121L, false);

            List<NoticeBoard> searchResult = List.of(noticeBoard1, noticeBoard2);
            NoticeBoardSearchDTO param = new NoticeBoardSearchDTO(LocalDateTime.now(), LocalDateTime.now(), "category", "key");
            PageRequest pageRequest = new PageRequest(5, 0, "1", "2");

            Integer totalCount = 10;

            //when
            Mockito.when(noticeBoardRepository.findNoneFixedNoticeBoardBySearchParam(any(), any(), anyInt()))
                    .thenReturn(searchResult);
            Mockito.when(noticeBoardRepository.findNoneFixedNoticeBoardCountBySearchParam(any(), anyInt()))
                    .thenReturn(totalCount);

            PageResponse<NoticeBoard> noneFixedNoticeBoards = noticeBoardService.getNoneFixedNoticeBoardSearch(param, pageRequest);

            //then
            Assertions.assertThat(noneFixedNoticeBoards.getContentSize()).isEqualTo(searchResult.size());
            Assertions.assertThat(noneFixedNoticeBoards.getTotalCount()).isEqualTo(totalCount);
            Assertions.assertThat(noneFixedNoticeBoards.getPageSize()).isEqualTo(pageRequest.getSize());
            Assertions.assertThat(noneFixedNoticeBoards.getCurrentPage()).isEqualTo(pageRequest.getCurrentPage());
        }
    }

    @Nested
    @DisplayName("게시글 식별자를 통해 공지 게시글 조회 테스트")
    public class GetNoticeBoardTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long noticeBoardId = 45641L;
            NoticeBoard noticeBoard = getSavedNoticeBoard(noticeBoardId);

            //when
            Mockito.when(noticeBoardRepository.findById(noticeBoardId))
                    .thenReturn(Optional.of(noticeBoard));

            NoticeBoard result = noticeBoardService.getNoticeBoardById(noticeBoardId);
            //then

            Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(noticeBoard);
        }

        @DisplayName("식별자에 해당하는 게시글이 없을때")
        @Test
        public void notExistBoard() throws Exception{
            //given
            //when
            Mockito.when(noticeBoardRepository.findById(any()))
                    .thenReturn(Optional.empty());
            //then
            Assertions.assertThatThrownBy(() -> noticeBoardService.getNoticeBoardById(-1L))
                    .isInstanceOf(EntityNotExistException.class);
        }
    }

    @Nested
    @DisplayName("게시글 식별자를 통해 공지 게시글 삭제 테스트")
    public class DeleteNoticeBoardById {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long noticeBoardId = 431564L;

            //when
            //then
            noticeBoardService.deleteNoticeBoardById(noticeBoardId);
        }
    }

    @Nested
    @DisplayName("게시글 식별자를 통해 공지 게시글 수정")
    public class EditNoticeBoardById {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long noticeBoardId = 431564L;
            NoticeBoard noticeBoard = NoticeBoard.builder()
                    .title("new")
                    .category("new C")
                    .isFixed(false)
                    .content("new Content")
                    .build();

            //when
            //then
            noticeBoardService.updateNoticeBoardById(noticeBoardId, noticeBoard);
        }
    }

    @Nested
    @DisplayName("공지 게시글 카테고리 가져오기")
    public class GetNoticeBoardCategory {
        @DisplayName("정상처리")
        @Test
        public void success() throws Exception {
            //given
            List<String> categories = List.of("A", "B", "c");

            //when
            Mockito.when(noticeBoardRepository.getCategory())
                    .thenReturn(categories);

            List<String> result = noticeBoardService.getCategory();
            //then

            Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(categories);
        }
    }

}