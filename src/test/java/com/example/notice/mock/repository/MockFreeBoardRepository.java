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

public class MockFreeBoardRepository implements FreeBoardRepository {

    private static final List<FreeBoard> repository = new ArrayList<>();

    public static FreeBoard SAVED_FREE_BOARD = FreeBoard.builder()
            .freeBoardId(1L)
            .title("title")
            .views(10L)
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
        throw new RuntimeException("mock 만들어야 한다.");
    }

}
