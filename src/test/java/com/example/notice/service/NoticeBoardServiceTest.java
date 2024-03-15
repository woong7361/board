package com.example.notice.service;

import com.example.notice.entity.NoticeBoard;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.mock.repository.MockNoticeBoardRepository;
import com.example.notice.mock.service.MockConfigurationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;


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
            noticeBoardService.createNoticeBoard(MockNoticeBoardRepository.NO_FK_NOTICE_BOARD, memberId);
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
            Assertions.assertThat(findNoticeBoard.size()).isEqualTo(configurationService.getMaxNoticeFixedCount());
        }
    }

}