package com.example.notice.mock.repository;

import com.example.notice.dto.FreeBoardSearchParam;
import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.page.PageRequest;
import com.example.notice.repository.FreeBoardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.notice.mock.database.MemoryDataBase.freeBoardRepository;

public class MockFreeBoardRepository implements FreeBoardRepository {

//    private final List<FreeBoard> freeBoardRepository = MemoryDataBase.freeBoardRepository;

    public static FreeBoard SAVED_FREE_BOARD = FreeBoard.builder()
            .freeBoardId(1L)
            .title("title")
            .views(11L)
            .content("content111")
            .category("CATEGORY")
            .createdAt(LocalDateTime.now())
            .member(MockMemberRepository.SAVED_MEMBER)
            .build();

    @Override
    public void save(FreeBoard freeBoard) {
        freeBoardRepository.add(freeBoard);
    }

    @Override
    public Optional<FreeBoard> findBoardById(Long freeBoardId) {
        List<FreeBoard> repository1 = freeBoardRepository;
        System.out.println("repository1 = " + repository1);
        return freeBoardRepository.stream()
                .filter((fd) -> fd.getFreeBoardId().equals(freeBoardId))
                .findFirst();
    }

    @Override
    public List<FreeBoard> findBoardsBySearchParam(FreeBoardSearchParam freeBoardSearchParam, PageRequest pageRequest) {
        return freeBoardRepository.stream()
                .filter((fd) -> {
                    boolean result = true;
                    if (freeBoardSearchParam.getCategory() != null) {
                        result = result && fd.getCategory().equals(freeBoardSearchParam.getCategory());
                    }
                    if (freeBoardSearchParam.getKeyWord() != null) {
                        result = result && fd.getContent().contains(freeBoardSearchParam.getKeyWord());
                        result = result && fd.getTitle().contains(freeBoardSearchParam.getKeyWord());
                        result = result && fd.getMemberName().contains(freeBoardSearchParam.getKeyWord());
                    }
                    if (freeBoardSearchParam.getStartDate() != null && freeBoardSearchParam.getEndDate() != null) {
                        result = result && fd.getCreatedAt().isBefore(freeBoardSearchParam.getEndDate());
                        result = result && fd.getCreatedAt().isAfter(freeBoardSearchParam.getStartDate());
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getTotalCountBySearchParam(FreeBoardSearchParam freeBoardSearchParam) {
        return (int) freeBoardRepository.stream()
                .filter((fd) -> {
                    boolean result = true;
                    if (freeBoardSearchParam.getCategory() != null) {
                        result = result && fd.getCategory().equals(freeBoardSearchParam.getCategory());
                    }
                    if (freeBoardSearchParam.getKeyWord() != null) {
                        result = result && fd.getContent().contains(freeBoardSearchParam.getKeyWord());
                        result = result && fd.getTitle().contains(freeBoardSearchParam.getKeyWord());
                        result = result && fd.getMemberName().contains(freeBoardSearchParam.getKeyWord());
                    }
                    if (freeBoardSearchParam.getStartDate() != null && freeBoardSearchParam.getEndDate() != null) {
                        result = result && fd.getCreatedAt().isBefore(freeBoardSearchParam.getEndDate());
                        result = result && fd.getCreatedAt().isAfter(freeBoardSearchParam.getStartDate());
                    }
                    return result;
                })
                .count();
    }

    @Override
    public void increaseViewsByBoardId(Long freeBoardId) {
        findBoardById(freeBoardId)
                .ifPresent((fd) -> {
                    freeBoardRepository.remove(fd);
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

                    freeBoardRepository.add(increaseBoard);
                });
    }

    @Override
    public boolean hasCommentByBoardId(Long freeBoardId) {
        return false;
    }

    @Override
    public void deleteContentAndMemberByBoardId(Long freeBoardId) {
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
        freeBoardRepository
                .removeIf((fd) -> fd.getFreeBoardId().equals(freeBoardId));
    }

    @Override
    public Optional<FreeBoard> findBoardByIdAndMemberId(Long freeBoardId, Long memberId) {
        return freeBoardRepository.stream()
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
                    save(updateBoard);
                });
    }

}
