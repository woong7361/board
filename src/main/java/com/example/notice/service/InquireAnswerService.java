package com.example.notice.service;

import com.example.notice.entity.InquireAnswer;

import java.util.List;

/**
 * 문의 게시글 답변 서비스
 */
public interface InquireAnswerService {

    /**
     * 문의 답변 생성
     *
     * @param inquireAnswer 문의 게시판 답변 생성 파라미터
     * @param inquireBoardId 문의 게시판 게시글 식별자
     * @param memberId 답변 생성자의 식별자
     * @return 생성된 답변 식별자
     */
    Long createAnswer(InquireAnswer inquireAnswer, Long inquireBoardId, Long memberId);

    /**
     * 문의 답변 삭제
     *
     * @param inquireAnswerId 문의 답변 식별자
     */
    void deleteById(Long inquireAnswerId);
}
