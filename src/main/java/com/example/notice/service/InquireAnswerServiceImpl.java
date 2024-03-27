package com.example.notice.service;

import com.example.notice.entity.InquireAnswer;
import com.example.notice.repository.InquireAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquireAnswerServiceImpl implements InquireAnswerService {

    private final InquireAnswerRepository inquireAnswerRepository;

    @Override
    @Transactional
    public Long createAnswer(InquireAnswer inquireAnswer, Long inquireBoardId, Long memberId) {
        inquireAnswerRepository.save(inquireAnswer, inquireBoardId, memberId);

        return inquireAnswer.getInquireAnswerId();
    }

    @Override
    @Transactional
    public void deleteById(Long inquireAnswerId) {
        inquireAnswerRepository.deleteById(inquireAnswerId);

    }
}
