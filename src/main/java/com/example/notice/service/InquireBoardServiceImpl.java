package com.example.notice.service;

import com.example.notice.entity.InquireBoard;
import com.example.notice.repository.InquireBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquireBoardServiceImpl implements InquireBoardService {

    private final InquireBoardRepository inquireBoardRepository;

    @Transactional
    @Override
    public Long createBoard(InquireBoard inquireBoard, Long memberId) {
        Long inquireBoardId = inquireBoardRepository.save(inquireBoard, memberId);

        return inquireBoardId;
    }

}
