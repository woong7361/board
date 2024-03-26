package com.example.notice.service;

import com.example.notice.entity.InquireAnswer;
import com.example.notice.repository.InquireAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquireAnswerServiceImpl implements InquireAnswerService {

    private final InquireAnswerRepository inquireAnswerRepository;

    @Override
    public Long createAnswer(InquireAnswer inquireAnswer, String inquireBoardId, Long memberId) {
        inquireAnswerRepository.save(inquireAnswer, inquireBoardId, memberId);

        return inquireAnswer.getInquireAnswerId();
    }
}
