package com.example.notice.mock.repository;

import com.example.notice.dto.NoticeBoardSearchParam;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.page.PageRequest;
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
    public List<NoticeBoard> findFixedNoticeBoardByLimit(Integer limit) {
        return NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> noticeBoard.getIsFixed().equals(true))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeBoard> findNoneFixedNoticeBoardBySearchParam(NoticeBoardSearchParam noticeBoardSearchParam, PageRequest pageRequest, Integer maxFixedNoticeCount) {
        List<Long> fixedIdList = NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> noticeBoard.getIsFixed().equals(true))
                .limit(maxFixedNoticeCount)
                .map(noticeBoard -> noticeBoard.getNoticeBoardId())
                .collect(Collectors.toList());

        return NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> !fixedIdList.contains(noticeBoard.getNoticeBoardId()))
                .filter((nb) -> {
                    boolean result = true;
                    if (noticeBoardSearchParam.getCategory() != null) {
                        result = result && nb.getCategory().equals(noticeBoardSearchParam.getCategory());
                    }
                    if (noticeBoardSearchParam.getKeyWord() != null) {
                        result = result && nb.getContent().contains(noticeBoardSearchParam.getKeyWord());
                        result = result && nb.getTitle().contains(noticeBoardSearchParam.getKeyWord());
                        result = result && nb.getMemberName().contains(noticeBoardSearchParam.getKeyWord());
                    }
                    if (noticeBoardSearchParam.getStartDate() != null && noticeBoardSearchParam.getEndDate() != null) {
                        result = result && nb.getCreatedAt().isBefore(noticeBoardSearchParam.getEndDate());
                        result = result && nb.getCreatedAt().isAfter(noticeBoardSearchParam.getStartDate());
                    }
                    return result;
                })
                .limit(pageRequest.getSize())
                .collect(Collectors.toList());
    }

    @Override
    public Integer findNoneFixedNoticeBoardCountBySearchParam(NoticeBoardSearchParam noticeBoardSearchParam, Integer maxFixedNoticeCount) {
        List<Long> fixedIdList = NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> noticeBoard.getIsFixed().equals(true))
                .limit(maxFixedNoticeCount)
                .map(noticeBoard -> noticeBoard.getNoticeBoardId())
                .collect(Collectors.toList());

        return (int) NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> !fixedIdList.contains(noticeBoard.getNoticeBoardId()))
                .filter((nb) -> {
                    boolean result = true;
                    if (noticeBoardSearchParam.getCategory() != null) {
                        result = result && nb.getCategory().equals(noticeBoardSearchParam.getCategory());
                    }
                    if (noticeBoardSearchParam.getKeyWord() != null) {
                        result = result && nb.getContent().contains(noticeBoardSearchParam.getKeyWord());
                        result = result && nb.getTitle().contains(noticeBoardSearchParam.getKeyWord());
                        result = result && nb.getMemberName().contains(noticeBoardSearchParam.getKeyWord());
                    }
                    if (noticeBoardSearchParam.getStartDate() != null && noticeBoardSearchParam.getEndDate() != null) {
                        result = result && nb.getCreatedAt().isBefore(noticeBoardSearchParam.getEndDate());
                        result = result && nb.getCreatedAt().isAfter(noticeBoardSearchParam.getStartDate());
                    }
                    return result;
                })
                .count();
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
