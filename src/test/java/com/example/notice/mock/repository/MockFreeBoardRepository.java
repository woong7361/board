package com.example.notice.mock.repository;

import com.example.notice.dto.request.FreeBoardSearchDTO;
import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;
import com.example.notice.page.PageRequest;
import com.example.notice.repository.FreeBoardRepository;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.notice.mock.database.MemoryDataBase.FREE_BOARD_STORAGE;

public class MockFreeBoardRepository implements FreeBoardRepository {

    public static FreeBoard SAVED_FREE_BOARD = FreeBoard.builder()
            .freeBoardId(1L)
            .title("title")
            .views(11L)
            .content("content111")
            .category("CATEGORY3")
            .createdAt(LocalDateTime.now())
            .modifiedAt(LocalDateTime.now())
            .member(MockMemberRepository.SAVED_MEMBER)
            .build();

    @Override
    public void save(FreeBoard freeBoard, Long memberId) {
        FREE_BOARD_STORAGE.add(freeBoard);
    }

    @Override
    public Optional<FreeBoard> findBoardById(Long freeBoardId) {
        return FREE_BOARD_STORAGE.stream()
                .filter((fd) -> fd.getFreeBoardId().equals(freeBoardId))
                .findFirst();
    }

    @Override
    public List<FreeBoard> findBoardsBySearchParam(FreeBoardSearchDTO freeBoardSearchDTO, PageRequest pageRequest) {
        return FREE_BOARD_STORAGE.stream()
                .filter((fd) -> {
                    boolean result = true;
                    if (freeBoardSearchDTO.getCategory() != null) {
                        result = result && fd.getCategory().equals(freeBoardSearchDTO.getCategory());
                    }
                    if (freeBoardSearchDTO.getKeyWord() != null) {
                        result = result && fd.getContent().contains(freeBoardSearchDTO.getKeyWord());
                        result = result && fd.getTitle().contains(freeBoardSearchDTO.getKeyWord());
                        result = result && fd.getMemberName().contains(freeBoardSearchDTO.getKeyWord());
                    }
                    if (freeBoardSearchDTO.getStartDate() != null && freeBoardSearchDTO.getEndDate() != null) {
                        result = result && fd.getCreatedAt().isBefore(freeBoardSearchDTO.getEndDate());
                        result = result && fd.getCreatedAt().isAfter(freeBoardSearchDTO.getStartDate());
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getTotalCountBySearchParam(FreeBoardSearchDTO freeBoardSearchDTO) {
        return (int) FREE_BOARD_STORAGE.stream()
                .filter((fd) -> {
                    boolean result = true;
                    if (freeBoardSearchDTO.getCategory() != null) {
                        result = result && fd.getCategory().equals(freeBoardSearchDTO.getCategory());
                    }
                    if (freeBoardSearchDTO.getKeyWord() != null) {
                        result = result && fd.getContent().contains(freeBoardSearchDTO.getKeyWord());
                        result = result && fd.getTitle().contains(freeBoardSearchDTO.getKeyWord());
                        result = result && fd.getMemberName().contains(freeBoardSearchDTO.getKeyWord());
                    }
                    if (freeBoardSearchDTO.getStartDate() != null && freeBoardSearchDTO.getEndDate() != null) {
                        result = result && fd.getCreatedAt().isBefore(freeBoardSearchDTO.getEndDate());
                        result = result && fd.getCreatedAt().isAfter(freeBoardSearchDTO.getStartDate());
                    }
                    return result;
                })
                .count();
    }

    @Override
    public void increaseViewsByBoardId(Long freeBoardId) {
        findBoardById(freeBoardId)
                .ifPresent((fd) -> {
                    FREE_BOARD_STORAGE.remove(fd);
                    FreeBoard increaseBoard = FreeBoard.builder()
                            .member(Member.builder().memberId(fd.getMemberId()).name(fd.getMemberName()).build())
                            .createdAt(fd.getCreatedAt())
                            .modifiedAt(fd.getModifiedAt())
                            .views(fd.getViews() + 1L)
                            .title(fd.getTitle())
                            .content(fd.getContent())
                            .category(fd.getCategory())
                            .freeBoardId(fd.getFreeBoardId())
                            .build();

                    FREE_BOARD_STORAGE.add(increaseBoard);
                });
    }

    @Override
    public boolean hasCommentByBoardId(Long freeBoardId) {
        return false;
    }

    @Override
    public void deleteContentAndTitleByBoardId(Long freeBoardId) {
        findBoardById(freeBoardId)
                .map((fd) -> FreeBoard.builder()
                        .member(Member.builder()
                                .memberId(fd.getMemberId())
                                .name(fd.getMemberName())
                                .build())
                        .modifiedAt(fd.getModifiedAt())
                        .createdAt(fd.getCreatedAt())
                        .title(fd.getTitle())
                        .content(fd.getContent())
                        .category(fd.getCategory())
                        .views(fd.getViews())
                        .build());
                }

    @Override
    public void deleteByBoardId(Long freeBoardId) {
        FREE_BOARD_STORAGE
                .removeIf((fd) -> fd.getFreeBoardId().equals(freeBoardId));
    }

    @Override
    public Optional<FreeBoard> findBoardByIdAndMemberId(Long freeBoardId, Long memberId) {
        return FREE_BOARD_STORAGE.stream()
                .filter((fd) -> fd.getFreeBoardId().equals(freeBoardId) && fd.getMemberId().equals(memberId))
                .findFirst();
    }

    @Override
    public void update(FreeBoard freeBoard, Long freeBoardId) {
        findBoardById(freeBoard.getFreeBoardId())
                .ifPresent((fd) -> {
                    deleteByBoardId(fd.getFreeBoardId());
                    FreeBoard updateBoard = FreeBoard.builder()
                            .freeBoardId(fd.getFreeBoardId())
                            .member(Member.builder()
                                    .memberId(fd.getMemberId())
                                    .name(fd.getMemberName())
                                    .build())
                            .createdAt(fd.getCreatedAt())
                            .modifiedAt(LocalDateTime.now())
                            .title(freeBoard.getTitle())
                            .content(freeBoard.getContent())
                            .category(freeBoard.getCategory())
                            .views(fd.getViews())
                            .build();
                    save(updateBoard, freeBoard.getMemberId());
                });
    }

    @Override
    public void deleteByAdmin(Long freeBoardId) {
        FREE_BOARD_STORAGE
                .removeIf((fd) -> fd.getFreeBoardId().equals(freeBoardId));
    }

    @Override
    public List<String> getCategory() {
        return List.of();
    }

}
