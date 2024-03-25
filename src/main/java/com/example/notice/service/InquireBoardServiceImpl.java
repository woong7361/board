package com.example.notice.service;

import com.example.notice.dto.InquireBoardSearchParam;
import com.example.notice.dto.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireBoard;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.repository.InquireBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquireBoardServiceImpl implements InquireBoardService {

    private final InquireBoardRepository inquireBoardRepository;

    @Transactional
    @Override
    public Long createBoard(InquireBoard inquireBoard, Long memberId) {
        return inquireBoardRepository.save(inquireBoard, memberId);
    }

    @Override
    public PageResponse<InquireBoardSearchResponseDTO> searchInquireBoard(InquireBoardSearchParam inquireBoardSearchParam, PageRequest pageRequest, Long memberId) {
        Integer searchTotalCount = inquireBoardRepository.getSearchTotalCount(inquireBoardSearchParam, memberId);

        List<InquireBoardSearchResponseDTO> inquireBoards =
                inquireBoardRepository.search(inquireBoardSearchParam, pageRequest, memberId);

        return new PageResponse<>(inquireBoards, pageRequest, searchTotalCount);
    }

}
