package com.example.notice.mock.repository;

import com.example.notice.entity.NoticeBoard;
import com.example.notice.repository.NoticeBoardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.notice.mock.database.MemoryDataBase.NOTICE_BOARD_STORAGE;

public class MockNoticeBoardRepository implements NoticeBoardRepository {

    public static NoticeBoard NO_FK_NOTICE_BOARD = NoticeBoard.builder()
            .noticeBoardId(1535L)
            .category("category1213")
            .title("title34322")
            .content("content15341")
            .isFixed(true)
            .views(0L)
            .createdAt(LocalDateTime.now())
            .build();


    @Override
    public void save(NoticeBoard noticeBoard, Long memberId) {
        NoticeBoard saveBoard = noticeBoardBuilderMapper(noticeBoard)
                .memberId(memberId)
                .build();
        NOTICE_BOARD_STORAGE.add(saveBoard);
    }

    @Override
    public List<NoticeBoard> findFixedNoticeBoardWithoutContentByLimit(Integer limit) {
        return NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> noticeBoard.getIsFixed().equals(true))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static NoticeBoard.NoticeBoardBuilder noticeBoardBuilderMapper(NoticeBoard noticeBoard) {
        return NoticeBoard.builder()
                .noticeBoardId(noticeBoard.getNoticeBoardId())
                .category(noticeBoard.getCategory())
                .title(noticeBoard.getTitle())
                .content(noticeBoard.getContent())
                .isFixed(noticeBoard.getIsFixed())
                .views(noticeBoard.getViews())
                .createdAt(noticeBoard.getCreatedAt())
                .memberId(noticeBoard.getMemberId());
    }
}
