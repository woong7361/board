package com.example.notice.service;

import com.example.notice.dto.request.FreeBoardSearchDTO;
import com.example.notice.entity.FreeBoard;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.exception.BoardNotExistException;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.repository.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.notice.constant.ErrorMessageConstant.AUTHORIZATION_EXCEPTION_MESSAGE;
import static com.example.notice.constant.ErrorMessageConstant.BOARD_NOT_EXIST_MESSAGE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService{

    private final FreeBoardRepository freeBoardRepository;

    @Override
    @Transactional
    public Long createFreeBoard(FreeBoard freeBoard, Long memberId) {
        freeBoardRepository.save(freeBoard, memberId);

        return freeBoard.getFreeBoardId();
    }

    @Transactional
    @Override
    public FreeBoard getBoardById(Long freeBoardId) {
        freeBoardRepository.increaseViewsByBoardId(freeBoardId);

        return freeBoardRepository.findBoardById(freeBoardId)
                .orElseThrow(() -> new BoardNotExistException(BOARD_NOT_EXIST_MESSAGE));
    }

    @Override
    public PageResponse<FreeBoard> getBoardsBySearchParams(FreeBoardSearchDTO freeBoardSearchDTO, PageRequest pageRequest) {
        Integer totalCount = freeBoardRepository.getTotalCountBySearchParam(freeBoardSearchDTO);
        List<FreeBoard> boards = freeBoardRepository.findBoardsBySearchParam(freeBoardSearchDTO, pageRequest);

        return new PageResponse<>(boards, pageRequest, totalCount);
    }

    /**
     * @implNote comment를 포함하고 있다면 내용과 제목만 지우고 comment는 남겨둔다.
     */
    @Transactional
    @Override
    public void deleteFreeBoardById(Long freeBoardId, Long memberId) {
        checkFreeBoardAuthorization(freeBoardId, memberId);

        if (freeBoardRepository.hasCommentByBoardId(freeBoardId)) {
            freeBoardRepository.deleteContentAndTitleByBoardId(freeBoardId);
        } else {
            freeBoardRepository.deleteByBoardId(freeBoardId);
        }

    }

    @Transactional
    @Override
    public void updateFreeBoardById(FreeBoard freeBoard, Long freeBoardId, Long memberId) {
        checkFreeBoardAuthorization(freeBoardId, memberId);

        freeBoardRepository.update(freeBoard, freeBoardId);
    }

    @Override
    @Transactional
    public void deleteFreeBoardByAdmin(Long freeBoardId, Long memberId) {
        freeBoardRepository.deleteByAdmin(freeBoardId);
    }

    @Override
    public List<String> getCategories() {
        return freeBoardRepository.getCategory();
    }

    private void checkFreeBoardAuthorization(Long freeBoardId, Long memberId) {
        freeBoardRepository.findBoardByIdAndMemberId(freeBoardId, memberId)
                .orElseThrow(() -> new AuthorizationException(AUTHORIZATION_EXCEPTION_MESSAGE));
    }
}



