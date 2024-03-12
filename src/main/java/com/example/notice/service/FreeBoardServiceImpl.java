package com.example.notice.service;

import com.example.notice.entity.FreeBoard;
import com.example.notice.exception.BoardNotExistException;
import com.example.notice.repository.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 자유 게시판 서비스 로직
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService{

    private final FreeBoardRepository freeBoardRepository;

    /**
     * 자유게시판의 게시글 저장
     * @param freeBoard 저장할 게시글
     * @return 게시글 식별자
     */
    @Override
    @Transactional
    public Long createFreeBoard(FreeBoard freeBoard) {
        freeBoardRepository.save(freeBoard);

        return freeBoard.getFreeBoardId();
    }

    /**
     * 자유게시판의 게시글을 게시글 식별자로 검색하여 가져온다.
     * @param freeBoardId 게시글 식별자
     * @return 게시글
     */
    @Override
    public FreeBoard getBoardById(Long freeBoardId) {
        return freeBoardRepository.findBoardById(freeBoardId)
                .orElseThrow(() -> new BoardNotExistException("not exist board"));
    }

}
