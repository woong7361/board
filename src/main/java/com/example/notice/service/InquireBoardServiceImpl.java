package com.example.notice.service;

import com.example.notice.dto.request.InquireBoardSearchDTO;
import com.example.notice.dto.response.InquireBoardResponseDTO;
import com.example.notice.dto.response.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireBoard;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.repository.InquireAnswerRepository;
import com.example.notice.repository.InquireBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.notice.constant.ErrorMessageConstant.AUTHORIZATION_EXCEPTION_MESSAGE;
import static com.example.notice.constant.ErrorMessageConstant.BOARD_NOT_EXIST_MESSAGE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquireBoardServiceImpl implements InquireBoardService {

    private final InquireBoardRepository inquireBoardRepository;
    private final InquireAnswerRepository inquireAnswerRepository;

    @Transactional
    @Override
    public Long createBoard(InquireBoard inquireBoard, Long memberId) {
        inquireBoardRepository.save(inquireBoard, memberId);

        return inquireBoard.getInquireBoardId();
    }

    @Override
    public PageResponse<InquireBoardSearchResponseDTO> searchInquireBoard(InquireBoardSearchDTO inquireBoardSearchDTO, PageRequest pageRequest) {
        Integer searchTotalCount = inquireBoardRepository.getSearchTotalCount(inquireBoardSearchDTO);

        List<InquireBoardSearchResponseDTO> inquireBoards =
                inquireBoardRepository.search(inquireBoardSearchDTO, pageRequest);

        return new PageResponse<>(inquireBoards, pageRequest, searchTotalCount);
    }

    @Override
    @Transactional
    public InquireBoardResponseDTO getBoardById(Long inquireBoardId, Long memberId) {
        InquireBoardResponseDTO result = inquireBoardRepository.findById(inquireBoardId)
                .orElseThrow(() -> new EntityNotExistException(BOARD_NOT_EXIST_MESSAGE));

        if (result.getInquireBoard().getIsSecret() && !result.getInquireBoard().getMemberId().equals(memberId)) {
            throw new AuthorizationException(AUTHORIZATION_EXCEPTION_MESSAGE);
        }

        inquireBoardRepository.increaseViewsById(inquireBoardId);
        return result;
    }

    @Override
    @Transactional
    public void updateById(InquireBoard inquireBoard, Long inquireBoardId, Long memberId) {
        checkInquireBoardAuthorization(inquireBoardId, memberId);

        inquireBoardRepository.updateById(inquireBoard, inquireBoardId);
    }

    @Override
    @Transactional
    public void deleteById(Long inquireBoardId, Long memberId) {
        checkInquireBoardAuthorization(inquireBoardId, memberId);

        inquireAnswerRepository.deleteByBoardId(inquireBoardId);
        inquireBoardRepository.deleteById(inquireBoardId);
    }

    @Override
    @Transactional
    public void deleteByAdmin(Long inquireBoardId) {
        inquireAnswerRepository.deleteByBoardId(inquireBoardId);
        inquireBoardRepository.deleteById(inquireBoardId);
    }

    @Override
    @Transactional
    public InquireBoardResponseDTO getBoardByAdmin(Long inquireBoardId) {
        InquireBoardResponseDTO result = inquireBoardRepository.findById(inquireBoardId)
                .orElseThrow(() -> new EntityNotExistException(BOARD_NOT_EXIST_MESSAGE));

        inquireBoardRepository.increaseViewsById(inquireBoardId);

        return result;

    }

    private void checkInquireBoardAuthorization(Long inquireBoardId, Long memberId) {
        inquireBoardRepository.findByInquireBoardIdAndMemberId(inquireBoardId, memberId)
                .orElseThrow(() -> new AuthorizationException(AUTHORIZATION_EXCEPTION_MESSAGE));
    }

}
