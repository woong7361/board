package com.example.notice.service;

import com.example.notice.dto.FreeBoardSearchParam;
import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;
import com.example.notice.exception.AuthorizationException;
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

    /**
     * @implNote 게시글의 조회수 +1
     */
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

    /**
     * @implNote 게시글에 댓글이 달려있다면 삭제하지 않고 게시글의 내용만 지운다.
     */
    @Transactional
    @Override
    public void deleteFreeBoard(Long freeBoardId, Member member) {
        freeBoardRepository.findBoardByIdAndMemberId(freeBoardId, member.getMemberId())
                .orElseThrow(() -> new AuthorizationException("삭제할 권한이 없습니다."));

        if (freeBoardRepository.hasCommentByBoardId(freeBoardId)) {
            freeBoardRepository.deleteContentAndMemberByBoardId(freeBoardId);
        } else {
            freeBoardRepository.deleteByBoardId(freeBoardId);
        }

    }

}
