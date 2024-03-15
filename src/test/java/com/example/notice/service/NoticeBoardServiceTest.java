package com.example.notice.service;

import com.example.notice.dto.NoticeBoardSearchParam;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.mock.repository.MockNoticeBoardRepository;
import com.example.notice.mock.service.MockConfigurationService;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.notice.mock.repository.MockNoticeBoardRepository.NO_FK_NOTICE_BOARD;


class NoticeBoardServiceTest {

    private MockNoticeBoardRepository noticeBoardRepository = new MockNoticeBoardRepository();
    private MockConfigurationService configurationService = new MockConfigurationService();
    private NoticeBoardService noticeBoardService = new NoticeBoardServiceImpl(noticeBoardRepository, configurationService);

    @BeforeEach
    public void clearRepository() {
        MemoryDataBase.clearNoticeBoardRepository();
    }

    @Nested
    @DisplayName("알림 게시판 생성 테스트")
    public class CreateNoticeBoard {
        @DisplayName("정상 처리")
        @Test

        public void success() throws Exception {
            //given
            Long memberId = 5L;
            //when
            noticeBoardService.createNoticeBoard(NO_FK_NOTICE_BOARD, memberId);
            //then
            Assertions.assertThat(MemoryDataBase.NOTICE_BOARD_STORAGE.size()).isEqualTo(1);
            Assertions.assertThat(MemoryDataBase.NOTICE_BOARD_STORAGE.get(0).getMemberId()).isEqualTo(memberId);
        }
    }

    @Nested
    @DisplayName("고정된 공지 게시글 조회 테스트")
    public class GetFixedNoticeBoard {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            int fixedSize = 4;
            for (int i = 0; i < fixedSize; i++) {
                NoticeBoard fixedBoard = NoticeBoard.builder()
                        .noticeBoardId(Long.parseLong(String.valueOf(i)))
                        .isFixed(true)
                        .build();
                noticeBoardRepository.save(fixedBoard, null);
            }
            for (int i = 1000; i < 1003; i++) {
                NoticeBoard notFixedBoard = NoticeBoard.builder()
                        .noticeBoardId(Long.parseLong(String.valueOf(i)))
                        .isFixed(false)
                        .build();
                noticeBoardRepository.save(notFixedBoard, null);
            }
            //when
            List<NoticeBoard> findNoticeBoard = noticeBoardService.getFixedNoticeBoardWithoutContent();
            //then
            Assertions.assertThat(findNoticeBoard.size()).isEqualTo(fixedSize);
        }

        @DisplayName("Limit 확인")
        @Test
        public void limit() throws Exception{
            //given
            int fixedSize = 56;
            for (int i = 0; i < fixedSize; i++) {
                NoticeBoard fixedBoard = NoticeBoard.builder()
                        .noticeBoardId(Long.parseLong(String.valueOf(i)))
                        .isFixed(true)
                        .build();
                noticeBoardRepository.save(fixedBoard, null);
            }
            for (int i = 1000; i < 1003; i++) {
                NoticeBoard notFixedBoard = NoticeBoard.builder()
                        .noticeBoardId(Long.parseLong(String.valueOf(i)))
                        .isFixed(false)
                        .build();
                noticeBoardRepository.save(notFixedBoard, null);
            }
            List<NoticeBoard> findNoticeBoard = noticeBoardService.getFixedNoticeBoardWithoutContent();

            //then
            Assertions.assertThat(findNoticeBoard.size()).isEqualTo(configurationService.getMaxFixedNoticeCount());
        }
    }


    //TODO 이자식도 내가 만든 repo를 test 해야하는 그런 느낌 -> 테스트를 하기 위해 repo를 만들고 그 repo를 또 test하고 또 거시기...
    @Nested
    @DisplayName("상단 고정 공지를 제외한 공지 검색 테스트")
    public class GetNoneFixedNoticeBoards {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            int fixedSize = 7;
            for (int i = 0; i < fixedSize; i++) {
                NoticeBoard fixedBoard = NoticeBoard.builder()
                        .noticeBoardId(Long.parseLong(String.valueOf(i)))
                        .isFixed(true)
                        .build();
                noticeBoardRepository.save(fixedBoard, null);
            }
            int unFixedSize = 3;
            for (int i = 1000; i < 1000 + unFixedSize; i++) {
                NoticeBoard notFixedBoard = NoticeBoard.builder()
                        .noticeBoardId(Long.parseLong(String.valueOf(i)))
                        .isFixed(false)
                        .build();
                noticeBoardRepository.save(notFixedBoard, null);
            }

            NoticeBoardSearchParam param =
                    new NoticeBoardSearchParam(null, null, null, null);
            PageRequest pageRequest = new PageRequest(5, 0, null, null);

            //when
            PageResponse<NoticeBoard> noneFixedNoticeBoards = noticeBoardService.getNoneFixedNoticeBoards(param, pageRequest);

            //then
            Assertions.assertThat(noneFixedNoticeBoards.getContentSize())
                    .isEqualTo(fixedSize + unFixedSize - configurationService.getMaxFixedNoticeCount());
        }
    }

    @Nested
    @DisplayName("게시글 식별자를 통해 공지 게시글 조회 테스트")
    public class GetNoticeBoardTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given

            //when
            noticeBoardRepository.save(NO_FK_NOTICE_BOARD, null);
            NoticeBoard findBoard = noticeBoardService.getNoticeBoardById(NO_FK_NOTICE_BOARD.getNoticeBoardId());
            //then

            Assertions.assertThat(findBoard)
                    .usingRecursiveComparison()
                    .isEqualTo(NO_FK_NOTICE_BOARD);
        }

        @DisplayName("식별자에 해당하는 게시글이 없을때")
        @Test
        public void notExistBoard() throws Exception{
            //given
            //when
            //then
            Assertions.assertThatThrownBy(() -> noticeBoardService.getNoticeBoardById(NO_FK_NOTICE_BOARD.getNoticeBoardId()))
                    .isInstanceOf(EntityNotExistException.class);
        }
    }

    @Nested
    @DisplayName("게시글 식별자를 통해 게시글 삭제 테스트")
    public class DeleteNoticeBoardById {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            noticeBoardRepository.save(NO_FK_NOTICE_BOARD, null);
            //when
            noticeBoardService.deleteNoticeBoardById(NO_FK_NOTICE_BOARD.getNoticeBoardId());
            //then
            Assertions.assertThat(noticeBoardRepository.findById(NO_FK_NOTICE_BOARD.getNoticeBoardId()).isEmpty())
                    .isEqualTo(true);
        }
    }

}