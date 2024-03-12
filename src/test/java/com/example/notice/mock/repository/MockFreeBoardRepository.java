package com.example.notice.mock.repository;

import com.example.notice.dto.FreeBoardSearchParam;
import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;
import com.example.notice.entity.MemberRole;
import com.example.notice.page.PageRequest;
import com.example.notice.repository.FreeBoardRepository;
import com.example.notice.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MockFreeBoardRepository implements FreeBoardRepository {

    private static final List<FreeBoard> repository = new ArrayList<>();

    public static FreeBoard SAVED_FREE_BOARD = FreeBoard.builder()
            .freeBoardId(1L)
            .title("title")
            .views(11L)
            .content("content111")
            .category("CATEGORY")
            .createdAt(LocalDateTime.now())
            .member(MockMemberRepository.SAVED_MEMBER)
            .build();

    static{
        repository.add(SAVED_FREE_BOARD);
    }

    @Override
    public void save(FreeBoard freeBoard) {
        repository.add(freeBoard);
    }

    @Override
    public Optional<FreeBoard> findBoardById(Long freeBoardId) {
        return repository.stream()
                .filter((fd) -> fd.getFreeBoardId().equals(freeBoardId))
                .findFirst();
    }

    @Override
    public List<FreeBoard> findBoardsBySearchParam(FreeBoardSearchParam freeBoardSearchParam, PageRequest pageRequest) {
        return repository.stream()
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
        return (int) repository.stream()
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
                    repository.remove(fd);
                    FreeBoard increaseBoard = FreeBoard.builder()
                            .member(Member.builder().memberId(fd.getMemberId()).name(fd.getMemberName()).build())
                            .createdAt(fd.getCreatedAt())
                            .modifiedAt(fd.getModifiedAt())
                            .views(fd.getViews() + 1)
                            .title(fd.getTitle())
                            .content(fd.getContent())
                            .category(fd.getCategory())
                            .freeBoardId(fd.getFreeBoardId())
                            .build();

                    repository.add(increaseBoard);
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
        repository
                .removeIf((fd) -> fd.getFreeBoardId().equals(freeBoardId));
    }

    @Override
    public Optional<FreeBoard> findBoardByIdAndMemberId(Long freeBoardId, Long memberId) {
        return repository.stream()
                .filter((fd) -> fd.getFreeBoardId().equals(freeBoardId) && fd.getMemberId().equals(memberId))
                .findFirst();
    }

}
