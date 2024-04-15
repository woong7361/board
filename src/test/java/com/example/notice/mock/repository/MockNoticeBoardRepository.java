package com.example.notice.mock.repository;

import com.example.notice.dto.request.NoticeBoardSearchDTO;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.page.PageRequest;
import com.example.notice.repository.NoticeBoardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
            .modifiedAt(LocalDateTime.now())
            .memberId(1534L)
            .memberName("name")
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
    public List<NoticeBoard> findNoneFixedNoticeBoardBySearchParam(NoticeBoardSearchDTO noticeBoardSearchDTO, PageRequest pageRequest, Integer maxFixedNoticeCount) {
        List<Long> fixedIdList = NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> noticeBoard.getIsFixed().equals(true))
                .limit(maxFixedNoticeCount)
                .map(noticeBoard -> noticeBoard.getNoticeBoardId())
                .collect(Collectors.toList());

        return NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> !fixedIdList.contains(noticeBoard.getNoticeBoardId()))
                .filter((nb) -> {
                    boolean result = true;
                    if (noticeBoardSearchDTO.getCategory() != null) {
                        result = result && nb.getCategory().equals(noticeBoardSearchDTO.getCategory());
                    }
                    if (noticeBoardSearchDTO.getKeyWord() != null) {
                        result = result && nb.getContent().contains(noticeBoardSearchDTO.getKeyWord());
                        result = result && nb.getTitle().contains(noticeBoardSearchDTO.getKeyWord());
                        result = result && nb.getMemberName().contains(noticeBoardSearchDTO.getKeyWord());
                    }
                    if (noticeBoardSearchDTO.getStartDate() != null && noticeBoardSearchDTO.getEndDate() != null) {
                        result = result && nb.getCreatedAt().isBefore(noticeBoardSearchDTO.getEndDate());
                        result = result && nb.getCreatedAt().isAfter(noticeBoardSearchDTO.getStartDate());
                    }
                    return result;
                })
                .limit(pageRequest.getSize())
                .collect(Collectors.toList());
    }

    @Override
    public Integer findNoneFixedNoticeBoardCountBySearchParam(NoticeBoardSearchDTO noticeBoardSearchDTO, Integer maxFixedNoticeCount) {
        List<Long> fixedIdList = NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> noticeBoard.getIsFixed().equals(true))
                .limit(maxFixedNoticeCount)
                .map(noticeBoard -> noticeBoard.getNoticeBoardId())
                .collect(Collectors.toList());

        return (int) NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> !fixedIdList.contains(noticeBoard.getNoticeBoardId()))
                .filter((nb) -> {
                    boolean result = true;
                    if (noticeBoardSearchDTO.getCategory() != null) {
                        result = result && nb.getCategory().equals(noticeBoardSearchDTO.getCategory());
                    }
                    if (noticeBoardSearchDTO.getKeyWord() != null) {
                        result = result && nb.getContent().contains(noticeBoardSearchDTO.getKeyWord());
                        result = result && nb.getTitle().contains(noticeBoardSearchDTO.getKeyWord());
                        result = result && nb.getMemberName().contains(noticeBoardSearchDTO.getKeyWord());
                    }
                    if (noticeBoardSearchDTO.getStartDate() != null && noticeBoardSearchDTO.getEndDate() != null) {
                        result = result && nb.getCreatedAt().isBefore(noticeBoardSearchDTO.getEndDate());
                        result = result && nb.getCreatedAt().isAfter(noticeBoardSearchDTO.getStartDate());
                    }
                    return result;
                })
                .count();
    }

    @Override
    public Optional<NoticeBoard> findById(Long noticeBoardId) {
        return NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> noticeBoard.getNoticeBoardId().equals(noticeBoardId))
                .findFirst();
    }

    @Override
    public void deleteById(Long noticeBoardId) {
        NOTICE_BOARD_STORAGE
                .removeIf(noticeBoard -> noticeBoard.getNoticeBoardId().equals(noticeBoardId));
    }

    @Override
    public void updateBoardById(Long noticeBoardId, NoticeBoard noticeBoard) {

    }

    @Override
    public void increaseViewsById(Long noticeBoardId) {
        Optional<NoticeBoard> findBoard = NOTICE_BOARD_STORAGE.stream()
                .filter(noticeBoard -> noticeBoard.getNoticeBoardId().equals(noticeBoardId))
                .findFirst();

        findBoard.ifPresent(noticeBoard -> {
                    deleteById(noticeBoard.getNoticeBoardId());
                    NoticeBoard increaseBoard = noticeBoardBuilderMapper(noticeBoard)
                            .views(noticeBoard.getViews() + 1)
                            .build();
                    NOTICE_BOARD_STORAGE.add(increaseBoard);
                });
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
