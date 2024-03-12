package com.example.notice.service;

import com.example.notice.dto.FreeBoardSearchParam;
import com.example.notice.entity.FreeBoard;
import com.example.notice.exception.BoardNotExistException;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.repository.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService{

    private final FreeBoardRepository freeBoardRepository;

    @Override
    @Transactional
    public Long createFreeBoard(FreeBoard freeBoard) {
        freeBoardRepository.save(freeBoard);

        return freeBoard.getFreeBoardId();
    }

    @Transactional
    @Override
    public FreeBoard getBoardById(Long freeBoardId) {
        freeBoardRepository.increaseViewsByBoardId(freeBoardId);

        return freeBoardRepository.findBoardById(freeBoardId)
                .orElseThrow(() -> new BoardNotExistException("not exist board"));
    }

    @Override
    public PageResponse<FreeBoard> getBoardsBySearchParams(FreeBoardSearchParam freeBoardSearchParam, PageRequest pageRequest) {
        Integer totalCount = freeBoardRepository.getTotalCountBySearchParam(freeBoardSearchParam);
        List<FreeBoard> boards =  freeBoardRepository.findBoardsBySearchParam(freeBoardSearchParam, pageRequest);

        return new PageResponse<>(boards, pageRequest, totalCount);
    }

}
